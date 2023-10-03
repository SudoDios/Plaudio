package routing.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.db.models.ModelFolder
import theme.ColorBox
import utils.Global
import utils.Tools.formatToDurationInfo

@Composable
fun SearchListSection(
    visiblePlayer : Boolean,
    maxWidth: Int,
    gridCount : Int
) {

    val searchKey = Global.Data.currentSearchKeyword.value
    val filteredList = when (Global.Data.searchType.value) {
        Global.Data.SEARCH_TYPE_FOLDERS -> {
            Global.Data.foldersList.filter { it.name.lowercase().contains(searchKey.lowercase()) }.sortedBy { it.name.lowercase() }
        }
        Global.Data.SEARCH_TYPE_ALBUMS -> {
            Global.Data.albumsList.filter { it.name.lowercase().contains(searchKey.lowercase()) }.sortedBy { it.name.lowercase() }
        }
        else -> {
            Global.Data.artistsList.filter { it.name.lowercase().contains(searchKey.lowercase()) }.sortedBy { it.name.lowercase() }
        }
    }

    if (filteredList.isEmpty()) {
        when (Global.Data.searchType.value) {
            Global.Data.SEARCH_TYPE_FOLDERS -> {
                EmptyList("icons/folder.svg","No folder found with this filter !!!")
            }
            Global.Data.SEARCH_TYPE_ALBUMS -> {
                EmptyList("icons/audio-album.svg","No album found with this filter !!!")
            }
            else -> {
                EmptyList("icons/mic.svg","No artist found with this filter !!!")
            }
        }
    } else {
        LazyVerticalGrid(columns = GridCells.Fixed(if (gridCount <= 0) 3 else gridCount),modifier = Modifier.fillMaxSize(),contentPadding = PaddingValues(
            start = 6.dp,
            end = 6.dp,
            top = (Global.APPBAR_HEIGHT + 6).dp,
            bottom = if (visiblePlayer && maxWidth < Global.SIZE_NORMAL) 80.dp else 6.dp
        )) {
            items(filteredList) {
                FolderItem(it)
            }
        }
    }

}

@Composable
private fun EmptyList (icon : String,description : String) {
    Column(Modifier.padding(top = Global.APPBAR_HEIGHT.dp).fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            modifier = Modifier.size(100.dp),
            painter = painterResource(icon),
            contentDescription = null,
            tint = ColorBox.text.copy(0.8f)
        )
        Spacer(Modifier.padding(10.dp))
        Text(
            text = description,
            color = ColorBox.text.copy(0.9f)
        )
    }
}

@Composable
private fun FolderItem (modelFolder: ModelFolder) {
    Column(Modifier.padding(6.dp).clip(RoundedCornerShape(16.dp)).background(ColorBox.text.copy(0.1f))
        .aspectRatio(1f)
        .clickable {
            Global.Data.currentFolder.value = modelFolder
            Global.Data.currentSearchKeyword.value = ""
            Global.Data.searchOrFilter()
        },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val icon = when (modelFolder.type) {
            ModelFolder.TYPE_FOLDER -> "icons/folder.svg"
            ModelFolder.TYPE_ARTIST -> "icons/mic.svg"
            ModelFolder.TYPE_ALBUM -> "icons/audio-album.svg"
            else -> ""
        }
        Icon(
            modifier = Modifier.size(56.dp),
            painter = painterResource(icon),
            contentDescription = null,
            tint = ColorBox.text.copy(0.8f)
        )
        Text(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp,top = 16.dp),
            text = modelFolder.name,
            maxLines = 2,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            color = ColorBox.text,
            fontSize = 15.sp
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "${modelFolder.childCunt} songs",
            color = ColorBox.text.copy(0.8f),
            fontSize = 13.sp
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = modelFolder.duration.formatToDurationInfo(),
            color = ColorBox.text.copy(0.7f),
            fontSize = 11.sp
        )
    }
}