package utils

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import core.CorePlayer
import core.db.CoreDB
import core.db.models.ModelAudio
import core.db.models.ModelFolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

object Global {

    const val APPBAR_HEIGHT = 164
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

        /*sort*/
        const val SORT_NAME_ASC = 0
        const val SORT_NAME_DESC = 1
        const val SORT_DURATION_ASC = 2
        const val SORT_DURATION_DESC = 3
        var sortType = mutableStateOf(SORT_NAME_ASC)

        /*search type*/
        const val SEARCH_TYPE_ALL = 0
        const val SEARCH_TYPE_FOLDERS = 1
        const val SEARCH_TYPE_ALBUMS = 2
        const val SEARCH_TYPE_ARTISTS = 3
        var searchType = mutableStateOf(SEARCH_TYPE_ALL)

        /*main*/
        private val audioList = ArrayList<ModelAudio>().apply {
            addAll(CoreDB.Audios.read(sortType.value))
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
        private val playlistsList = SnapshotStateList<ModelFolder>()

        /*states*/
        var currentSearchKeyword = mutableStateOf("")
        var currentFolder = mutableStateOf(ModelFolder())

        /*search & filter*/
        fun refreshAudioList () {
            audioList.clear()
            audioList.addAll(CoreDB.Audios.read(sortType.value))
        }
        fun setData (changeCurrentFolder : Boolean = true,reFilter : Boolean = true) {
            foldersList.clear()
            albumsList.clear()
            artistsList.clear()
            playlistsList.clear()

            allAudiosFolder.value = ModelFolder(childCunt = audioList.size, duration = audioList.sumOf { it.duration })
            favoritesFolder.value = ModelFolder(name = "Favorites", path = "#Fav", childCunt = audioList.count { it.isFav },duration = audioList.filter { it.isFav }.sumOf { it.duration })


            audioList.groupingBy { it.folder }.eachCount().forEach {
                val filter = audioList.filter { filter -> filter.folder == it.key }
                val sumDuration = filter.sumOf { sum -> sum.duration }
                foldersList.add(ModelFolder(name = it.key.substringAfterLast(File.separator), type = ModelFolder.TYPE_FOLDER, path = it.key, childCunt = it.value, duration = sumDuration))
            }
            foldersList.sortBy { it.name.lowercase() }
            audioList.groupingBy { it.album }.eachCount().forEach {
                val filter = audioList.filter { filter -> filter.album == it.key }
                val sumDuration = filter.sumOf { sum -> sum.duration }
                albumsList.add(ModelFolder(name = it.key, type = ModelFolder.TYPE_ALBUM, path = it.key, childCunt = it.value, duration = sumDuration))
            }
            albumsList.sortBy { it.name.lowercase() }
            audioList.groupingBy { it.artist }.eachCount().forEach {
                val filter = audioList.filter { filter -> filter.artist == it.key }
                val sumDuration = filter.sumOf { sum -> sum.duration }
                artistsList.add(ModelFolder(name = it.key, type = ModelFolder.TYPE_ARTIST, path = it.key, childCunt = it.value, duration = sumDuration))
            }
            artistsList.sortBy { it.name.lowercase() }
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
                    searchType.value = SEARCH_TYPE_ALL
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

        fun reFetchMainList () {
            audioList.clear()
            audioList.addAll(CoreDB.Audios.read(sortType.value))
            setData(false, reFilter = true)
        }

        fun addOrRemoveFavorite (hash : String,isFav : Boolean) {
            val indexFilter = filteredAudioList.indexOfFirst { it.hash == hash }
            if (indexFilter != -1) {
                filteredAudioList[indexFilter] = filteredAudioList[indexFilter].copy(isFav = isFav)
            }

            if (CorePlayer.currentMediaItemCallback.value.hash == hash) {
                CorePlayer.currentMediaItemCallback.value = CorePlayer.currentMediaItemCallback.value.copy(isFav = isFav)
            }

            val indexMain = audioList.indexOfFirst { it.hash == hash }
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
            val indexFilter = filteredAudioList.indexOfFirst { it.id == modelAudio.id }
            if (indexFilter != -1) {
                filteredAudioList[indexFilter] = modelAudio
            }

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