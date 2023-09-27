package core.extractor

import core.db.CoreDB
import core.db.models.ModelAudio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import utils.Tools.md5
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.isDirectory
import kotlin.io.path.isHidden
import kotlin.io.path.name

object AudioStore {

    private val supportedFormats = arrayListOf("mp3","m4a","ogg")
    private val arrayFind = ArrayList<ModelAudio>()
    lateinit var callback: Callback

    fun findAudios (dir: String) {
        CoroutineScope(Dispatchers.IO).launch {
            arrayFind.clear()
            val oldAudioList = CoreDB.Audios.read()
            if (oldAudioList.isEmpty()) {
                //find & save
                recursiveWalk(dir)
                arrayFind.forEach {
                    CoreDB.Audios.insert(it)
                }
                if (this@AudioStore::callback.isInitialized) {
                    callback.onFinished()
                    arrayFind.clear()
                }
            } else {
                //find & merge new files
                recursiveWalk(dir)
                val favoriteAudios = oldAudioList.filter { it.isFav }.map { it.hash }
                val playCounts = oldAudioList.associate { Pair(it.hash,it.playCount) }
                CoreDB.Audios.clearAll()
                arrayFind.forEach {
                    if (favoriteAudios.contains(it.hash)) {
                        it.isFav = true
                    }
                    it.playCount = if (playCounts[it.hash] == null) 0 else playCounts[it.hash]!!
                    CoreDB.Audios.insert(it)
                }
                if (this@AudioStore::callback.isInitialized) {
                    callback.onFinished()
                    arrayFind.clear()
                }
            }
        }
    }

    private fun recursiveWalk (dir : String) {
        Files.walk(Paths.get(dir)).forEach {
            if (!it.isDirectory() && !it.isHidden()) {
                val format = it.name.substringAfterLast(".").lowercase()
                if (supportedFormats.contains(format)) {
                    val file = it.toFile()
                    val modelAudio = AudioInfo.getInfo(file)
                    if (modelAudio != null && modelAudio.duration > 9000) {
                        modelAudio.hash = file.md5()
                        arrayFind.add(modelAudio)
                    }
                    if (this@AudioStore::callback.isInitialized) {
                        callback.onCounting(arrayFind)
                    }
                }
            }
        }
    }

    interface Callback {
        fun onCounting (audios : ArrayList<ModelAudio>)
        fun onFinished ()
    }

}