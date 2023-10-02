package core.extractor

import core.db.CoreDB
import core.db.models.ModelAudio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import utils.Tools.md5
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.isReadable
import kotlin.io.path.isWritable
import kotlin.io.path.name

object AudioStore {

    private val supportedFormats = arrayListOf("mp3","m4a","ogg","wav","flac")
    private val arrayFind = ArrayList<ModelAudio>()
    lateinit var callback: Callback

    private var countFolderSearch = 0

    fun findAudios (dir: String) {
        countFolderSearch = 0
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
        Files.walkFileTree(Paths.get(dir),object : SimpleFileVisitor<Path>() {
            override fun preVisitDirectory(dir: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                countFolderSearch++
                if (this@AudioStore::callback.isInitialized) {
                    callback.onCountingFolder(countFolderSearch)
                }
                return super.preVisitDirectory(dir, attrs)
            }
            override fun visitFile(fileIn: Path, attrs: BasicFileAttributes?): FileVisitResult {
                if (fileIn.isWritable() && fileIn.isReadable()) {
                    val format = fileIn.name.substringAfterLast(".").lowercase()
                    if (supportedFormats.contains(format)) {
                        val file = fileIn.toFile()
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
                return super.visitFile(fileIn, attrs)
            }
            override fun postVisitDirectory(dir: Path?, exc: IOException?): FileVisitResult {
                if (exc is AccessDeniedException) {
                    return FileVisitResult.SKIP_SUBTREE
                }
                return super.postVisitDirectory(dir, exc)
            }
            override fun visitFileFailed(file: Path?, exc: IOException?): FileVisitResult {
                if (exc is AccessDeniedException) {
                    return FileVisitResult.SKIP_SUBTREE
                }
                return super.visitFileFailed(file, exc)
            }
        })
    }

    interface Callback {
        fun onCounting (audios : ArrayList<ModelAudio>)
        fun onFinished ()
        fun onCountingFolder (count : Int)
    }

}