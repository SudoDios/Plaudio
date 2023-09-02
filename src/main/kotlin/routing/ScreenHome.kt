package routing

import CenterState.audioList
import CenterState.currentFolder
import CenterState.filteredAudioList
import CenterState.folderList
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import components.CustomScaffold
import components.rememberCustomOCState
import core.db.models.ModelAudio
import core.db.models.ModelFolder
import kotlinx.coroutines.launch
import routing.dialogs.PlayerBottomSheet
import routing.sections.Appbar
import routing.sections.CompactPlayer
import routing.sections.Content
import routing.sections.Drawer
import theme.ColorBox

@Composable
fun ScreenHome() {

    var currentSearchKeyword by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val drawerState = rememberCustomOCState()
    val sheetState = rememberCustomOCState()
    val searchState = rememberCustomOCState()

    CustomScaffold(
        drawerState = drawerState,
        sheetState = sheetState,
        searchState = searchState,
        scope = scope,
        drawerBackgroundColor = ColorBox.primaryDark2,
        appbarBackgroundColor = ColorBox.primaryDark,
        appbarContent = {
            Appbar(currentFolder.value, onSearchClicked = {
                scope.launch {
                    drawerState.close()
                    searchState.open()
                }
            })
        },
        mainContent = {
            Content(
                audioList = filteredAudioList,
                onFav = { id, isFav  ->
                    val indexMain = audioList.indexOfFirst { it.id == id }
                    audioList[indexMain].isFav = isFav

                    val newChildCount = if (isFav) folderList[1].childCunt + 1 else folderList[1].childCunt - 1
                    folderList[1] = folderList[1].copy(childCunt = newChildCount)
                    if (currentFolder.value.path == "#Fav") {
                        currentFolder.value = currentFolder.value.copy(childCunt = newChildCount)
                        filterList(currentSearchKeyword, currentFolder.value, filteredAudioList, audioList)
                    }
                },
                onEdited = { editedAudio ->
                    val indexMain = audioList.indexOfFirst { it.id == editedAudio.id }
                    audioList[indexMain] = editedAudio
                }
            )
            CompactPlayer(
                onClicked = {
                    scope.launch {
                        sheetState.open()
                    }
                }
            )
        },
        onSearchContent = {
            currentSearchKeyword = it
            filterList(currentSearchKeyword, currentFolder.value, filteredAudioList, audioList)
        },
        drawerContent = {
            Drawer(
                folders = folderList,
                selectedFolder = currentFolder.value.path
            ) {
                scope.launch {
                    drawerState.close()
                }
                currentFolder.value = it
                filterList(currentSearchKeyword, currentFolder.value, filteredAudioList, audioList)
            }
        },
        sheetContent = {
            PlayerBottomSheet {
                scope.launch {
                    sheetState.close()
                }
            }
        }
    )

}


private fun filterList(
    keyword: String,
    modelFolder: ModelFolder,
    filterList: SnapshotStateList<ModelAudio>,
    mainList: ArrayList<ModelAudio>
) {
    filterList.clear()
    if (keyword.trim().isNotEmpty()) {
        filterList.addAll(mainList.filter { it.name.lowercase().contains(keyword.lowercase()) })
    } else {
        when (modelFolder.path) {
            "#All" -> filterList.addAll(mainList)
            "#Fav" -> filterList.addAll(mainList.filter { it.isFav })
            else -> filterList.addAll(mainList.filter { it.folder == modelFolder.path })
        }
    }
}
