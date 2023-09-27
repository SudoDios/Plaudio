package utils

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import core.db.CoreDB
import core.db.models.ModelAudio
import core.db.models.ModelFolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Global {

    const val APPBAR_HEIGHT = 132
    const val SIZE_SMALL = 600
    const val SIZE_NORMAL = 1100
    const val SIZE_LARGE = 1450
    const val SIZE_HEIGHT_LARGE = 830

    val userHome : String get() = System.getProperty("user.home")
    val coverPaths : String get() = "$userHome/.plaudio/covers"
    val dbPath : String get() = "$userHome/.plaudio/db"
    val prefsPath : String get() = "$userHome/.plaudio/prefs"

    object Data {

        var hasAnyTextFieldFocus = false

        /*main*/
        private val audioList = ArrayList<ModelAudio>().apply {
            addAll(CoreDB.Audios.read())
        }
        val mostPlayedAudios = SnapshotStateList<ModelAudio>().apply {
            addAll(CoreDB.Audios.readMostPlayed())
        }
        val filteredAudioList = SnapshotStateList<ModelAudio>().apply {
            addAll(audioList)
        }

        /*drawer*/
        var allAudiosFolder = mutableStateOf(ModelFolder())
        var favoritesFolder = mutableStateOf(ModelFolder())
        val foldersList = SnapshotStateList<ModelFolder>()
        val albumsList = SnapshotStateList<ModelFolder>()
        val artistsList = SnapshotStateList<ModelFolder>()
        val playlistsList = SnapshotStateList<ModelFolder>()

        /*states*/
        var currentSearchKeyword = mutableStateOf("")
        var currentFolder = mutableStateOf(ModelFolder())

        /*search & filter*/
        fun refreshAudioList () {
            audioList.clear()
            audioList.addAll(CoreDB.Audios.read())
        }
        fun setData (changeCurrentFolder : Boolean = true,reFilter : Boolean = true) {
            foldersList.clear()
            albumsList.clear()
            artistsList.clear()
            playlistsList.clear()
            allAudiosFolder.value = ModelFolder(childCunt = audioList.size)
            favoritesFolder.value = ModelFolder(name = "Favorites", path = "#Fav", childCunt = audioList.count { it.isFav })
            audioList.groupingBy { it.folder }.eachCount().forEach {
                foldersList.add(ModelFolder(name = it.key.substringAfterLast("/"), type = ModelFolder.TYPE_FOLDER, path = it.key, childCunt = it.value))
            }
            audioList.groupingBy { it.album }.eachCount().forEach {
                albumsList.add(ModelFolder(name = it.key, type = ModelFolder.TYPE_ALBUM, path = it.key, childCunt = it.value))
            }
            audioList.groupingBy { it.artist }.eachCount().forEach {
                artistsList.add(ModelFolder(name = it.key, type = ModelFolder.TYPE_ARTIST, path = it.key, childCunt = it.value))
            }
            if (changeCurrentFolder) currentFolder.value = allAudiosFolder.value
            if (reFilter) searchOrFilter()
        }

        init {
            setData(changeCurrentFolder = true, reFilter = false)
        }


        fun searchOrFilter() {
            CoroutineScope(Dispatchers.IO).launch {
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
                        else -> {
                            when (currentFolder.value.type) {
                                ModelFolder.TYPE_FOLDER -> {
                                    filteredAudioList.addAll(audioList.filter { it.folder == currentFolder.value.path })
                                }
                                ModelFolder.TYPE_ARTIST -> {
                                    filteredAudioList.addAll(audioList.filter { it.artist == currentFolder.value.path })
                                }
                                ModelFolder.TYPE_ALBUM -> {
                                    filteredAudioList.addAll(audioList.filter { it.album == currentFolder.value.path })
                                }
                            }
                        }
                    }
                }
            }
        }

        fun addOrRemoveFavorite (id : Int,isFav : Boolean) {
            val indexMain = audioList.indexOfFirst { it.id == id }
            audioList[indexMain].isFav = isFav
            val newFavCount = if (isFav) {
                favoritesFolder.value.childCunt + 1
            } else {
                favoritesFolder.value.childCunt - 1
            }
            favoritesFolder.value = favoritesFolder.value.copy(childCunt = newFavCount)
            if (currentFolder.value.path == "#Fav") {
                currentFolder.value = currentFolder.value.copy(childCunt = newFavCount)
                searchOrFilter()
            }
        }

        fun editedAudio (modelAudio: ModelAudio) {
            val indexMain = audioList.indexOfFirst { it.id == modelAudio.id }
            audioList[indexMain] = modelAudio
            val indexMostPlayed = mostPlayedAudios.indexOfFirst { it.id == modelAudio.id }
            if (indexMostPlayed != -1) {
                mostPlayedAudios[indexMostPlayed] = modelAudio
            }
            setData(false, reFilter = true)
        }

    }

}