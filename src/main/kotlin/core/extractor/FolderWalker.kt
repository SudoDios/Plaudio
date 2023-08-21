package core.extractor

import core.AudioInfo
import core.db.CoreDB
import core.db.models.ModelAudio
import core.db.models.ModelFolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class FolderWalker(private var callback : Callback) {

    private val supportedFormats = arrayListOf("mp3","m4a","ogg")

    private val listFolders = mutableMapOf<String,Int>()
    private var count = 0

    fun findAudios (dir: File) {
        CoroutineScope(Dispatchers.IO).launch {
            if (CoreDB.Audios.read().isEmpty()) {
                recursiveWalk(dir)
                listFolders.forEach {
                    val modelFolderItem = ModelFolder(
                        name = it.key.substringAfterLast("/"),
                        path = it.key,
                        childCunt = it.value
                    )
                    CoreDB.Folders.insert(modelFolderItem)
                }
            }
            val audios = CoreDB.Audios.read()
            val folders = CoreDB.Folders.read()
            callback.onCounting(audios.size)
            callback.onFinished(ArrayList(audios),folders)
        }
    }

    /**
     * extract audio files from folders
     * */
    private fun recursiveWalk (dir : File) {
        val lsFiles = dir.listFiles() ?: return
        for (x in lsFiles) {
            if (x.isDirectory && !x.isHidden) {
                //is folder and is not hidden
                callback.onSearching(x.path)
                recursiveWalk(x)
            }
            if (x.isHidden || !x.canRead()) {
                //is hidden or not have permissions
                continue
            } else {
                val format = x.name.substringAfterLast(".").lowercase()
                if (!supportedFormats.contains(format)) {
                    continue
                } else {
                    //is audio file for my result
                    val modelAudio = AudioInfo.getInfo2(x)
                    if (modelAudio != null && modelAudio.duration > 9000) {
                        CoreDB.Audios.insert(modelAudio)
                        var folderCount = listFolders[x.parentFile.absolutePath]
                        if (folderCount == null) {
                            listFolders[x.parentFile.absolutePath] = 1
                        } else {
                            folderCount++
                            listFolders[x.parentFile.absolutePath] = folderCount
                        }
                        count++
                        callback.onCounting(count)
                    } else {
                        continue
                    }
                }
            }
        }
    }

    interface Callback {
        fun onSearching (folder : String)
        fun onCounting (count : Int)
        fun onFinished (audios : ArrayList<ModelAudio>,folders : ArrayList<ModelFolder>)
    }

}