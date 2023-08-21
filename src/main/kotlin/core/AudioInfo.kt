package core

import core.db.models.ModelAudio
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import utils.Tools
import java.io.File
import java.util.concurrent.TimeUnit

object AudioInfo {

    fun getInfo2 (file : File) : ModelAudio? {
        try {
            val audioFile = AudioFileIO.read(file)
            val modelAudio = ModelAudio()
            modelAudio.name = audioFile.tag.getFirst(FieldKey.TITLE).titleFix(file.name.substringBeforeLast("."))
            modelAudio.artist = audioFile.tag.getFirst(FieldKey.ARTIST).artistNameFix()
            if (audioFile.tag.firstArtwork != null) {
                val mime = audioFile.tag.firstArtwork.mimeType
                modelAudio.coverArt = Tools.writeThumbImage(audioFile.tag.firstArtwork.binaryData,mime)
            }
            modelAudio.path = file.absolutePath
            modelAudio.size = file.length()
            modelAudio.folder = file.parentFile.absolutePath
            modelAudio.duration = TimeUnit.SECONDS.toMillis(audioFile.audioHeader.trackLength.toLong())
            modelAudio.format = file.name.substringAfterLast(".").lowercase()
            return modelAudio
        } catch (_ : Exception) {
            return null
        }
    }

    private fun String.titleFix (fileName : String) : String {
        return trim().ifEmpty {
            fileName
        }
    }

    private fun String.artistNameFix () : String {
        return trim().ifEmpty {
            "Unknown"
        }
    }

}