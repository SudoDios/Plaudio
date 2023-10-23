package utils.spectrum

import core.db.CoreDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import utils.Global
import utils.Tools
import utils.Tools.runCommand
import java.io.File
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import kotlin.math.abs

object WaveformGenerator {

    private const val BUFFER_SIZE = 4096
    private lateinit var threadScope: Job

    fun generate (path : String,hash : String,callback : (FloatArray) -> Unit) {
        if (this::threadScope.isInitialized && threadScope.isActive) {
            threadScope.cancel()
        }
        threadScope = CoroutineScope(Dispatchers.IO).launch {
            File(Global.convertPath).deleteRecursively()
            File(Global.convertPath).mkdirs()
            val localWave = CoreDB.Waveforms.getWaveform(hash)
            if (localWave == null) {
                val ext = path.substringAfterLast(".")
                if (ext.lowercase() != "wav") {
                    val convertedFile = File(Global.convertPath,"${Tools.getRandomString()}.wav")
                    try {
                        listOf("ffmpeg","-i",path,convertedFile.absolutePath).runCommand()
                        if (convertedFile.exists())
                            generateWaveform(convertedFile) {
                                CoreDB.Waveforms.saveWaveform(hash,it)
                                callback.invoke(it)
                            }
                    } catch (_ : Exception) {}
                } else {
                    generateWaveform(File(path)) {
                        CoreDB.Waveforms.saveWaveform(hash,it)
                        callback.invoke(it)
                    }
                }
            } else {
                callback.invoke(localWave)
            }
        }
    }

    private fun generateWaveform (file: File,callback: (FloatArray) -> Unit) {
        try {
            val samples = getWavSamples(file)
            val amps = processAmplitudes(samples)
            callback.invoke(amps)
        } catch (_ : Exception) { }
    }

    private fun getWavSamples(file: File): IntArray {
        try {
            AudioSystem.getAudioInputStream(file).use { input ->
                val baseFormat = input.format

                val encoding = AudioFormat.Encoding.PCM_UNSIGNED
                val sampleRate = baseFormat.sampleRate
                val numChannels = baseFormat.channels
                val decodedFormat = AudioFormat(encoding, sampleRate, 16, numChannels, numChannels * 2, sampleRate, false)
                val available = input.available()

                try {
                    AudioSystem.getAudioInputStream(decodedFormat, input).use { pcmDecodedInput ->
                        val buffer = ByteArray(BUFFER_SIZE)

                        val maximumArrayLength = 100000
                        val finalAmplitudes = IntArray(maximumArrayLength)
                        val samplesPerPixel = available / maximumArrayLength

                        var currentSampleCounter = 0
                        var arrayCellPosition = 0
                        var currentCellValue = 0.0f

                        var arrayCellValue: Int

                        while (pcmDecodedInput.readNBytes(buffer, 0, BUFFER_SIZE) > 0) {
                            var i = 0
                            while (i < buffer.size - 1) {
                                arrayCellValue = ((buffer[i + 1].toInt() shl 8 or (buffer[i]
                                    .toInt() and 0xff) shl 16) / 32767 * 1.3).toInt()

                                if (currentSampleCounter != samplesPerPixel) {
                                    ++currentSampleCounter
                                    currentCellValue += abs(arrayCellValue)
                                } else {
                                    if (arrayCellPosition != maximumArrayLength) {
                                        finalAmplitudes[arrayCellPosition + 1] =
                                            currentCellValue.toInt() / samplesPerPixel
                                        finalAmplitudes[arrayCellPosition] = finalAmplitudes[arrayCellPosition + 1]
                                    }
                                    currentSampleCounter = 0
                                    currentCellValue = 0f
                                    arrayCellPosition += 2
                                }
                                i += 2
                            }
                        }
                        return finalAmplitudes
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return IntArray(1)
    }


    private fun processAmplitudes(sourcePcmData: IntArray): FloatArray {
        val size = 60
        val waveData = FloatArray(size)
        val samplesPerPixel = sourcePcmData.size / size
        var nValue: Float
        for (w in 0 until size) {
            val c = w * samplesPerPixel
            nValue = 0.0f
            for (s in 0 until samplesPerPixel) {
                nValue += abs(sourcePcmData[c + s].toFloat()) / 65536.0f
            }
            waveData[w] = nValue / samplesPerPixel
        }
        return waveData
    }

}