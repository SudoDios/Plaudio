package routing.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.CircleDot
import components.CustomOcState
import components.HamburgerMenu
import components.MyIconButton
import components.menu.SortListPopup
import core.db.models.ModelFolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import theme.ColorBox
import utils.Global
import utils.Tools.formatToDurationInfo
import utils.clickable

@Composable
fun Appbar(
    showMenuIcon: Boolean,
    drawerState: CustomOcState,
    scope: CoroutineScope,
) {

    Column(
        Modifier.height(Global.APPBAR_HEIGHT.dp).clickable().fillMaxWidth().background(ColorBox.primaryDark.copy(0.4f))
    ) {
        Row(
            Modifier.padding(start = 16.dp, top = 8.dp, end = 8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showMenuIcon) {
                HamburgerMenu(modifier = Modifier.size(48.dp), size = 20.dp, isOpen = drawerState.isOpen) {
                    if (drawerState.isOpen) {
                        scope.launch {
                            drawerState.close()
                        }
                    } else {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                }
                Spacer(Modifier.padding(6.dp))
            }
            SearchTextField()
        }
        AnimatedVisibility(
            visible = Global.Data.currentSearchKeyword.value.isEmpty(),
            enter = fadeIn(tween(200, easing = LinearEasing)),
            exit = fadeOut(tween(200, easing = LinearEasing))
        ) {
            NormalHead()
        }
        AnimatedVisibility(
            visible = Global.Data.currentSearchKeyword.value.isNotEmpty(),
            enter = fadeIn(tween(200, easing = LinearEasing)),
            exit = fadeOut(tween(200, easing = LinearEasing))
        ) {
            SearchHead()
        }
    }
}

@Composable
private fun SearchTextField() {

    Box(
        Modifier.height(48.dp).widthIn(max = 450.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp))
            .background(ColorBox.card.copy(0.8f))
            .drawBehind {
                if (Global.Data.currentSearchKeyword.value.trim().isNotEmpty()) {
                    drawRect(color = ColorBox.text.copy(0.1f))
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(start = 12.dp).size(20.dp),
                painter = painterResource("icons/search-normal.svg"),
                contentDescription = null,
                tint = ColorBox.text.copy(0.6f)
            )
            BasicTextField(
                modifier = Modifier.padding(12.dp).weight(1f).onFocusChanged { Global.Data.hasAnyTextFieldFocus = it.hasFocus },
                value = Global.Data.currentSearchKeyword.value,
                textStyle = TextStyle(
                    color = ColorBox.text.copy(0.8f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                ),
                cursorBrush = SolidColor(ColorBox.text.copy(0.9f)),
                singleLine = true,
                onValueChange = {
                    Global.Data.currentSearchKeyword.value = it
                    Global.Data.searchOrFilter()
                }
            )
            if (Global.Data.currentSearchKeyword.value.trim().isNotEmpty()) {
                MyIconButton(
                    icon = "icons/close.svg",
                    onClick = {
                        Global.Data.currentSearchKeyword.value = ""
                        Global.Data.searchOrFilter()
                    }
                )
            }
        }
        if (Global.Data.currentSearchKeyword.value.trim().isEmpty()) {
            Text(
                modifier = Modifier.padding(start = 43.dp).fillMaxWidth(),
                text = "Search anything ...",
                fontSize = 15.sp,
                color = ColorBox.text.copy(0.3f)
            )
        }
    }

}

@Composable
private fun NormalHead() {

    var showSortMenu by remember { mutableStateOf(false) }

    Row(Modifier.padding(start = 16.dp, top = 14.dp), verticalAlignment = Alignment.CenterVertically) {
        val currentFolder = Global.Data.currentFolder.value
        val icon = when {
            currentFolder.path == "#All" -> "icons/music-note-2.svg"
            currentFolder.path == "#Fav" -> "icons/heart-bold.svg"
            currentFolder.type == ModelFolder.TYPE_FOLDER -> "icons/folder.svg"
            currentFolder.type == ModelFolder.TYPE_ARTIST -> "icons/mic.svg"
            currentFolder.type == ModelFolder.TYPE_ALBUM -> "icons/audio-album.svg"
            else -> ""
        }
        val tint = if (currentFolder.path == "#Fav") Color.Red else ColorBox.text
        Icon(
            modifier = Modifier.size(80.dp).padding(12.dp),
            painter = painterResource(icon),
            contentDescription = null,
            tint = tint
        )
        Column(Modifier.padding(start = 16.dp, end = 16.dp).weight(1f)) {
            Text(
                modifier = Modifier.padding(end = 12.dp),
                textAlign = TextAlign.Center,
                text = Global.Data.currentFolder.value.name,
                color = ColorBox.text,
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.padding(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${Global.Data.currentFolder.value.childCunt} songs",
                    color = ColorBox.text.copy(0.7f),
                    fontSize = 14.sp
                )
                CircleDot(Modifier.padding(start = 8.dp, end = 8.dp).size(4.dp), color = ColorBox.text.copy(0.6f))
                Text(
                    text = Global.Data.currentFolder.value.duration.formatToDurationInfo(),
                    color = ColorBox.text.copy(0.7f),
                    fontSize = 14.sp
                )
            }
        }
        Row(
            Modifier.padding(end = 16.dp)
                .height(44.dp)
                .clip(RoundedCornerShape(50))
                .background(ColorBox.text.copy(0.1f))
                .clickable {
                    showSortMenu = true
                }.padding(start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val sortModel = when (Global.Data.sortType.value) {
                Global.Data.SORT_NAME_ASC -> Pair("icons/sort-asc.svg", "Name")
                Global.Data.SORT_NAME_DESC -> Pair("icons/sort-desc.svg", "Name")
                Global.Data.SORT_DURATION_ASC -> Pair("icons/sort-asc.svg", "Duration")
                else -> Pair("icons/sort-desc.svg", "Duration")
            }
            Icon(
                modifier = Modifier.size(22.dp),
                painter = painterResource(sortModel.first),
                contentDescription = null,
                tint = ColorBox.text
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = sortModel.second,
                color = ColorBox.text,
                fontSize = 13.sp
            )
        }
    }

    SortListPopup(
        show = showSortMenu,
        onDismissRequest = {
            showSortMenu = false
        }
    )
}

@Composable
private fun SearchHead() {

    Row(Modifier.padding(start = 10.dp, top = 14.dp), verticalAlignment = Alignment.CenterVertically) {
        SearchTypeItem(
            selected = Global.Data.searchType.value == Global.Data.SEARCH_TYPE_ALL,
            icon = "icons/music-note-2.svg",
            name = "All Audios",
            onClicked = {
                Global.Data.searchType.value = Global.Data.SEARCH_TYPE_ALL
            }
        )
        SearchTypeItem(
            selected = Global.Data.searchType.value == Global.Data.SEARCH_TYPE_FOLDERS,
            icon = "icons/folder.svg",
            name = "Folders",
            onClicked = {
                Global.Data.searchType.value = Global.Data.SEARCH_TYPE_FOLDERS
            }
        )
        SearchTypeItem(
            selected = Global.Data.searchType.value == Global.Data.SEARCH_TYPE_ALBUMS,
            icon = "icons/audio-album.svg",
            name = "Albums",
            onClicked = {
                Global.Data.searchType.value = Global.Data.SEARCH_TYPE_ALBUMS
            }
        )
        SearchTypeItem(
            selected = Global.Data.searchType.value == Global.Data.SEARCH_TYPE_ARTISTS,
            icon = "icons/mic.svg",
            name = "Artists",
            onClicked = {
                Global.Data.searchType.value = Global.Data.SEARCH_TYPE_ARTISTS
            }
        )
    }

}

@Composable
private fun SearchTypeItem(selected: Boolean, icon: String, name: String, onClicked: () -> Unit) {
    Column(
        Modifier.padding(start = 6.dp, end = 6.dp).width(100.dp)
            .height(80.dp).clip(RoundedCornerShape(16.dp)).background(ColorBox.card)
            .drawBehind {
                if (selected) {
                    drawRect(color = ColorBox.text.copy(0.1f))
                }
            }
            .clickable {
                onClicked.invoke()
            },
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = ColorBox.text.copy(0.8f)
        )
        AnimatedVisibility(
            visible = selected
        ) {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = name,
                color = ColorBox.text.copy(0.8f),
                fontSize = 13.sp
            )
        }
    }
}

