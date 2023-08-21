package routing.sections

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.db.models.ModelFolder
import theme.ColorBox

@Composable
fun Drawer(folders: SnapshotStateList<ModelFolder>, selectedFolder: String = "#All",callback : (selected : ModelFolder) -> Unit) {

    LazyColumn(contentPadding = PaddingValues(6.dp)) {
        items(folders) {
            FolderItem(it, selectedFolder == it.path) {
                callback.invoke(it)
            }
            if (it.path == "#Fav") {
                Divider(Modifier.padding(top = 6.dp, bottom = 6.dp).fillMaxWidth(), color = ColorBox.text.copy(0.2f))
            }
        }
    }

}

@Composable
private fun FolderItem(modelFolder: ModelFolder, isSelected: Boolean, onClick: () -> Unit) {

    val icon = when (modelFolder.path) {
        "#All" -> "icons/audio-folder.svg"
        "#Fav" -> "icons/heart.svg"
        else -> "icons/folder.svg"
    }

    val color = when (modelFolder.path) {
        "#All" -> ColorBox.text
        "#Fav" -> Color.Red
        else -> ColorBox.text.copy(0.6f)
    }

    val animate = animateFloatAsState(if (isSelected) 0.1f else 0f)

    Row(
        modifier = Modifier.fillMaxWidth().height(44.dp).clip(RoundedCornerShape(50))
            .background(ColorBox.text.copy(animate.value)).clickable {
                onClick.invoke()
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.padding(7.dp))
        Icon(
            modifier = Modifier.size(23.dp),
            painter = painterResource(icon),
            contentDescription = null,
            tint = color
        )
        Spacer(Modifier.padding(7.dp))
        Text(text = "${modelFolder.name} (${modelFolder.childCunt})", fontSize = 14.sp, color = ColorBox.text.copy(0.8f))
    }

}