package routing.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.CircleDot
import components.MyIconButton
import core.db.models.ModelFolder
import routing.dialogs.AboutDialog
import routing.dialogs.BaseDialog
import routing.dialogs.SearchAudioDialog
import routing.dialogs.YTDownloaderDialog
import theme.ColorBox
import theme.Fonts
import ui.components.DayNightAnimationIcon
import utils.Global
import utils.clickable

@Composable
fun Drawer(callback: () -> Unit) {

    Column(
        modifier = Modifier
            .width(300.dp)
            .fillMaxHeight()
            .background(ColorBox.primaryDark2)
    ) {
        DrawerHeader()
        DrawerBody(callback)
        YoutubeButton()
        DrawerFooter()
    }

}

@Composable
private fun DrawerHeader() {
    Row(
        Modifier.fillMaxWidth().padding(start = 24.dp, top = 24.dp, bottom = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(40.dp).clip(RoundedCornerShape(50)).background(ColorBox.text.copy(0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource("icons/app_icon.svg"),
                contentDescription = null,
                tint = ColorBox.text
            )
        }
        Spacer(Modifier.padding(6.dp))
        Text(
            text = "Plaudio",
            style = TextStyle.Default.copy(
                fontFamily = Fonts.varela,
                brush = Brush.linearGradient(listOf(ColorBox.text, ColorBox.text.copy(0.5f))),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                letterSpacing = 3.sp
            )
        )
    }
}

@Composable
private fun ColumnScope.DrawerBody(callback: () -> Unit) {
    var expandedItem by rememberSaveable { mutableStateOf("") }

    Column(Modifier.padding(start = 4.dp, end = 4.dp).fillMaxWidth().weight(1f).verticalScroll(rememberScrollState())) {
        DrawerSingleItem(
            icon = "icons/music-note-2.svg",
            modelFolder = Global.Data.allAudiosFolder.value,
            onClick = {
                Global.Data.currentFolder.value = Global.Data.allAudiosFolder.value
                Global.Data.searchOrFilter()
                callback.invoke()
            }
        )
        DrawerSingleItem(
            icon = "icons/heart-bold.svg",
            iconTint = Color.Red,
            modelFolder = Global.Data.favoritesFolder.value,
            onClick = {
                Global.Data.currentFolder.value = Global.Data.favoritesFolder.value
                Global.Data.searchOrFilter()
                callback.invoke()
            }
        )
        Divider(
            Modifier.padding(top = 6.dp, bottom = 6.dp).fillMaxWidth(),
            color = ColorBox.text.copy(0.2f)
        )
        DrawerItem(
            expandedItem = expandedItem,
            icon = "icons/folder.svg",
            name = "Folders",
            size = Global.Data.foldersList.size,
            content = Global.Data.foldersList,
            callback = callback,
            onClick = {
                expandedItem = if (expandedItem == it) "" else it
            }
        )
        DrawerItem(
            expandedItem = expandedItem,
            icon = "icons/audio-album.svg",
            name = "Albums",
            size = Global.Data.albumsList.size,
            content = Global.Data.albumsList,
            callback = callback,
            onClick = {
                expandedItem = if (expandedItem == it) "" else it
            }
        )
        DrawerItem(
            expandedItem = expandedItem,
            icon = "icons/mic.svg",
            name = "Artists",
            size = Global.Data.artistsList.size,
            content = Global.Data.artistsList,
            callback = callback,
            onClick = {
                expandedItem = if (expandedItem == it) "" else it
            }
        )
    }
}

@Composable
private fun YoutubeButton() {

    var showDialog by remember { mutableStateOf(false) }

    Row(Modifier.padding(12.dp).fillMaxWidth().height(48.dp).clip(RoundedCornerShape(50))
        .background(ColorBox.YOUTUBE_COLOR.copy(0.1f))
        .clickable {
            showDialog = true
        }, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.padding(start = 12.dp),
            painter = painterResource("icons/youtube.svg"),
            contentDescription = null,
            tint = ColorBox.YOUTUBE_COLOR
        )
        Text(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            text = "YouTube Downloader",
            fontSize = 14.sp,
            color = ColorBox.text
        )
    }

    //Yt-dl
    BaseDialog(
        expanded = showDialog,
        onDismissRequest = {
            showDialog = false
        }
    ) {
        YTDownloaderDialog {
            showDialog = false
        }
    }
}

@Composable
private fun DrawerFooter() {

    var showSyncDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }

    Row(Modifier.fillMaxWidth().background(ColorBox.card).clickable()) {
        Row(
            Modifier
                .padding(12.dp)
                .height(44.dp)
                .weight(1f)
                .clip(RoundedCornerShape(50))
                .background(ColorBox.text.copy(0.1f))
                .clickable {
                    ColorBox.switchDarkLight()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            DayNightAnimationIcon(
                Modifier.padding(start = 8.dp).size(28.dp),
                color = ColorBox.text,
                isDay = ColorBox.isDarkMode
            )
            Text(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                text = "Switch theme",
                fontSize = 14.sp,
                color = ColorBox.text
            )
        }
        MyIconButton(
            size = 44.dp,
            contentPadding = 10.dp,
            padding = PaddingValues(top = 12.dp, bottom = 12.dp, end = 6.dp),
            icon = "icons/refresh.svg"
        ) {
            showSyncDialog = true
        }
        MyIconButton(
            size = 44.dp,
            contentPadding = 10.dp,
            padding = PaddingValues(top = 12.dp, bottom = 12.dp, end = 8.dp),
            icon = "icons/info.svg"
        ) {
            showInfoDialog = true
        }
    }

    BaseDialog(
        expanded = showSyncDialog
    ) {
        SearchAudioDialog(
            onFinished = {
                showSyncDialog = false
                Global.Data.refreshAudioList()
                Global.Data.setData(false, reFilter = true)
            },
            onClose = {
                showSyncDialog = false
            }
        )
    }

    BaseDialog(
        expanded = showInfoDialog,
        onDismissRequest = {
            showInfoDialog = false
        }
    ) {
        AboutDialog {
            showInfoDialog = false
        }
    }
}


@Composable
private fun DrawerSingleItem(
    icon: String,
    modelFolder: ModelFolder,
    onClick: () -> Unit,
    iconTint: Color = ColorBox.primary.copy(0.8f),
) {

    val isSelected = Global.Data.currentFolder.value.path == modelFolder.path
    val backgroundColor = if (isSelected) ColorBox.primary.copy(0.15f) else Color.Transparent

    Row(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .clickable {
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.padding(7.dp))
        AnimatedVisibility(
            visible = isSelected
        ) {
            CircleDot(Modifier.padding(end = 8.dp).size(6.dp), color = ColorBox.primary)
        }
        Icon(
            modifier = Modifier.size(23.dp),
            painter = painterResource(icon),
            contentDescription = null,
            tint = iconTint
        )
        Spacer(Modifier.padding(7.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = "${modelFolder.name} (${modelFolder.childCunt})",
            fontSize = 14.sp,
            color = ColorBox.text.copy(0.9f)
        )
    }
}

@Composable
private fun DrawerItem(
    expandedItem: String = "",
    icon: String,
    name: String,
    size: Int,
    onClick: (String) -> Unit,
    callback: () -> Unit,
    iconTint: Color = ColorBox.primary.copy(0.8f),
    content: SnapshotStateList<ModelFolder> = SnapshotStateList()
) {

    val animateFraction = animateFloatAsState(if (expandedItem == name) 1f else 0f)
    val expandBackColor =
        androidx.compose.ui.graphics.lerp(Color.Transparent, ColorBox.primary.copy(0.05f), animateFraction.value)

    val isSelected = content.any { it.type == Global.Data.currentFolder.value.type && it.path == Global.Data.currentFolder.value.path }

    Column(Modifier.padding(6.dp).fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(expandBackColor)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(50))
                .clickable {
                    onClick.invoke(name)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.padding(7.dp))
            AnimatedVisibility(
                visible = isSelected
            ) {
                CircleDot(Modifier.padding(end = 8.dp).size(6.dp), color = ColorBox.primary)
            }
            Icon(
                modifier = Modifier.size(23.dp),
                painter = painterResource(icon),
                contentDescription = null,
                tint = iconTint
            )
            Spacer(Modifier.padding(7.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = "$name ($size)",
                fontSize = 14.sp,
                color = ColorBox.text.copy(0.9f)
            )
            Icon(
                modifier = Modifier.padding(end = 12.dp).size(20.dp).rotate(180 * animateFraction.value),
                painter = painterResource("icons/arrow-down.svg"),
                contentDescription = null,
                tint = ColorBox.text.copy(0.8f)
            )
        }
        AnimatedVisibility(
            visible = expandedItem == name
        ) {

            Column(Modifier.fillMaxWidth()) {
                if (name == "Playlists") {
                    Row(Modifier.fillMaxWidth().height(44.dp).clickable {
                        //create playlist
                    }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource("icons/add-circle.svg"),
                            contentDescription = null,
                            tint = ColorBox.text.copy(0.8f)
                        )
                        Text(
                            modifier = Modifier.padding(start = 12.dp),
                            text = "Create Playlist",
                            color = ColorBox.text.copy(0.8f),
                            fontSize = 13.sp
                        )
                    }
                }
                content.forEach {
                    DrawerSubItem(it, onClick = {
                        Global.Data.currentFolder.value = it
                        Global.Data.searchOrFilter()
                        callback.invoke()
                    })
                }
            }
        }
    }

}

@Composable
private fun DrawerSubItem(
    modelFolder: ModelFolder,
    onClick: () -> Unit
) {

    val currentSel = Global.Data.currentFolder.value
    val backgroundColor =
        if (currentSel.path == modelFolder.path && currentSel.type == modelFolder.type) ColorBox.primary.copy(0.1f) else Color.Transparent

    Row(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .clickable {
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.padding(7.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = "${modelFolder.name} (${modelFolder.childCunt})",
            fontSize = 13.sp,
            color = ColorBox.text.copy(0.8f)
        )
    }
}
