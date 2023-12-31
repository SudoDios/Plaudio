package utils

import androidx.compose.ui.awt.ComposeDialog
import java.awt.FileDialog
import java.io.File
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.swing.SwingUtilities
import kotlin.math.log10
import kotlin.math.pow

object Tools {

    fun getOvershotInterpolator (tension : Float = 2f,t : Float) : Float {
        var s = t
        s -= 1.0f
        return s * s * ((tension + 1) * s + tension) + 1.0f
    }

    fun getRandomString(length: Int = 12) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun Long.formatToDuration(): String {
        val mFormatBuilder: StringBuilder = StringBuilder()
        val mFormatter = Formatter(mFormatBuilder, Locale.ENGLISH)
        val totalSeconds = this / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        mFormatBuilder.setLength(0)
        return if (hours > 0) {
            mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }

    fun Long.formatToDurationInfo(): String {
        val totalSeconds = this / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        val strBuilder = StringBuilder()
        if (hours > 0) strBuilder.append("${hours}h ")
        if (minutes > 0) strBuilder.append("${minutes}m ")
        if (seconds > 0) strBuilder.append("${seconds}s")
        return strBuilder.toString()
    }

    fun Long.formatToSizeFile(): String {
        val symbols = DecimalFormatSymbols(Locale.ENGLISH)
        if (this <= 0) return "0"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#", symbols).format(
            this / 1024.0.pow(digitGroups.toDouble())
        ) + " " + units[digitGroups]
    }

    fun Long.formatToCount(): String {
        val fmt = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT)
        return fmt.format(this)
    }

    fun postDelayed (delay : Long,callback : () -> Unit) {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                SwingUtilities.invokeLater {
                    callback.invoke()
                }
            }
        },delay)
    }

    fun Double.roundPlace (decimalPlace: Int): Double {
        return BigDecimal(this.toString()).setScale(decimalPlace,RoundingMode.HALF_UP).toDouble()
    }

    fun getRepeatModeIconByType(type : Int) : String {
        return when (type) {
            Prefs.REPEAT_MODE_ALL -> "icons/repeat-all.svg"
            Prefs.REPEAT_MODE_ONE -> "icons/repeat-one.svg"
            Prefs.REPEAT_MODE_SHUFFLE -> "icons/shuffle.svg"
            else -> "icons/stop.svg"
        }
    }

    fun List<String>.runCommand(): String? {
        return try {
            val proc = ProcessBuilder(*this.toTypedArray())
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()
            proc.waitFor(10, TimeUnit.SECONDS)
            proc.inputStream.bufferedReader().readText()
        } catch(_: IOException) {
            null
        }
    }

    fun openFilePicker (title : String = "Choice an image",vararg filterFormats: String) : File? {
        val fileGet = FileDialog(ComposeDialog(),title, FileDialog.LOAD)
        fileGet.setFilenameFilter { _, name -> filterFormats.contains(name.substringAfterLast(".")) }
        fileGet.title = "Choice an image for cover art"
        fileGet.isVisible = true
        fileGet.isMultipleMode = false
        return if (fileGet.files.isEmpty()) {
            null
        } else {
            fileGet.files[0]
        }
    }

}