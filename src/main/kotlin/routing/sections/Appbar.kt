package routing.sections

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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.CircleDot
import components.CustomOcState
import components.HamburgerMenu
import components.menu.SortListPopup
import core.db.models.ModelFolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import theme.ColorBox
import utils.Global
import utils.Tools.formatToDuration
import utils.Tools.formatToDurationInfo
import utils.clickable

@Composable
fun Appbar(
    showMenuIcon: Boolean,
    drawerState: CustomOcState,
    scope: CoroutineScope,
) {

    Column(Modifier.height(Global.APPBAR_HEIGHT.dp).clickable().fillMaxWidth().background(ColorBox.primaryDark.copy(0.4f)), verticalArrangement = Arrangement.Center) {
        Row(Modifier.padding(start = 16.dp, top = 8.dp,end = 8.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
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
            searchTextField {
                Global.Data.currentSearchKeyword.value = it
                Global.Data.searchOrFilter()
            }
        }
        NormalHead()
    }
}


@Composable
private fun NormalHead() {

    var showSortMenu by remember { mutableStateOf(false) }

    Spacer(Modifier.padding(5.dp))
    Row(Modifier.padding(start = 16.dp),verticalAlignment = Alignment.CenterVertically) {
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
                CircleDot(Modifier.padding(start = 8.dp, end = 8.dp).size(4.dp),color = ColorBox.text.copy(0.6f))
                Text(
                    text = Global.Data.currentFolder.value.duration.formatToDurationInfo(),
                    color = ColorBox.text.copy(0.7f),
                    fontSize = 14.sp
                )
            }
        }
        Row(Modifier.padding(end = 16.dp)
            .height(44.dp)
            .clip(RoundedCornerShape(50))
            .background(ColorBox.text.copy(0.1f))
            .clickable {
                showSortMenu = true
            }.padding(start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val sortModel = when (Global.Data.sortType.value) {
                Global.Data.SORT_NAME_ASC -> Pair("icons/sort-asc.svg","Name")
                Global.Data.SORT_NAME_DESC -> Pair("icons/sort-desc.svg","Name")
                Global.Data.SORT_DURATION_ASC -> Pair("icons/sort-asc.svg","Duration")
                else -> Pair("icons/sort-desc.svg","Duration")
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
private fun searchTextField(
    onValueChange: (String) -> Unit,
) {

    var value by remember { mutableStateOf(TextFieldValue()) }

    Box(
        Modifier.height(48.dp).widthIn(max = 450.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(ColorBox.card.copy(0.8f)),
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
                modifier = Modifier.padding(12.dp).weight(1f)
                    .onFocusChanged { Global.Data.hasAnyTextFieldFocus = it.hasFocus },
                value = value,
                textStyle = TextStyle(
                    color = ColorBox.text.copy(0.8f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                ),
                cursorBrush = SolidColor(ColorBox.text.copy(0.9f)),
                singleLine = true,
                onValueChange = {
                    value = it
                    onValueChange.invoke(it.text)
                }
            )

        }
        if (value.text.trim().isEmpty()) {
            Text(
                modifier = Modifier.padding(start = 43.dp).fillMaxWidth(),
                text = "Search audios ...",
                fontSize = 15.sp,
                color = ColorBox.text.copy(0.3f)
            )
        }
    }

}