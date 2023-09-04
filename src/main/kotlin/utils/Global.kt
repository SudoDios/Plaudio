package utils

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import core.CorePlayer
import core.db.CoreDB
import core.db.models.ModelAudio
import core.db.models.ModelFolder

object Global {

    const val APPBAR_HEIGHT = 132
    const val SIZE_SMALL = 600
    const val SIZE_NORMAL = 810
    const val SIZE_LARGE = 1300
    const val SIZE_HEIGHT_LARGE = 800

    val userHome : String get() = System.getProperty("user.home")
    val coverPaths : String get() = "$userHome/.plaudio/covers"
    val dbPath : String get() = "$userHome/.plaudio/db"


    object Data {

        var hasAnyTextFieldFocus = false

        val audioList = ArrayList<ModelAudio>().apply {
            addAll(CoreDB.Audios.read())
        }
        val filteredAudioList = SnapshotStateList<ModelAudio>().apply {
            addAll(audioList)
        }
        val folderList = SnapshotStateList<ModelFolder>().apply {
            addAll(CoreDB.Folders.read())
        }

        /*states*/
        var currentSearchKeyword = mutableStateOf("")
        var currentFolder = mutableStateOf(ModelFolder(childCunt = audioList.size))

        /*search & filter*/
        fun searchOrFilter() {
            filteredAudioList.clear()
            if (currentSearchKeyword.value.trim().isNotEmpty()) {
                filteredAudioList.addAll(audioList.filter {
                    it.name.lowercase().contains(currentSearchKeyword.value.lowercase()) ||
                            it.artist.lowercase().contains(currentSearchKeyword.value.lowercase())
                })
            } else {
                when (currentFolder.value.path) {
                    "#All" -> filteredAudioList.addAll(audioList)
                    "#Fav" -> filteredAudioList.addAll(audioList.filter { it.isFav })
                    else -> filteredAudioList.addAll(audioList.filter { it.folder == currentFolder.value.path })
                }
            }
            CorePlayer.audioList = ArrayList(filteredAudioList)
        }

        /*favorite*/
        fun addOrRemoveFavorite (id : Int,isFav : Boolean) {
            val indexMain = audioList.indexOfFirst { it.id == id }
            audioList[indexMain].isFav = isFav

            val newChildCount = if (isFav) folderList[1].childCunt + 1 else folderList[1].childCunt - 1
            folderList[1] = folderList[1].copy(childCunt = newChildCount)
            if (currentFolder.value.path == "#Fav") {
                currentFolder.value = currentFolder.value.copy(childCunt = newChildCount)
                searchOrFilter()
            }
        }

        fun editedAudio (modelAudio: ModelAudio) {
            val indexMain = audioList.indexOfFirst { it.id == modelAudio.id }
            audioList[indexMain] = modelAudio
        }

    }


}