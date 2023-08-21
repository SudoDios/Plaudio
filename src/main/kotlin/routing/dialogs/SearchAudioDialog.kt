package routing.dialogs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.AnimatedText
import components.SearchAnimIcon
import core.db.models.ModelAudio
import core.db.models.ModelFolder
import core.extractor.FolderWalker
import theme.ColorBox
import utils.Tools
import java.io.File

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchAudioDialog(onFinished : (audios: SnapshotStateList<ModelAudio>, folders: SnapshotStateList<ModelFolder>) -> Unit) {

    var findCount by remember { mutableStateOf(0) }
    var searchedContent by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        FolderWalker(object : FolderWalker.Callback{
            override fun onSearching(folder: String) {
                searchedContent = folder
            }
            override fun onCounting(count: Int) {
                findCount = count
            }
            override fun onFinished(audios: ArrayList<ModelAudio>, folders: ArrayList<ModelFolder>) {
                onFinished.invoke(SnapshotStateList<ModelAudio>().apply { addAll(audios) }, SnapshotStateList<ModelFolder>().apply { addAll(folders) })
            }
        }).findAudios(File(Tools.userHome))
    }

    Column(
        modifier = Modifier.width(290.dp).background(ColorBox.primaryDark2),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchAnimIcon(Modifier.padding(top = 30.dp).size(60.dp))
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = "Searching for audios ...",
            color = ColorBox.text,
            fontSize = 15.sp
        )
        Row(
            modifier = Modifier.padding(top = 16.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedText(
                modifier = Modifier.padding(),
                text = findCount.toString(),
                color = ColorBox.primary,
                fontSize = 13.sp
            )
            Text(
                modifier = Modifier.padding(),
                text = " items were found",
                color = ColorBox.text.copy(0.7f),
                fontSize = 12.sp
            )
        }
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 25.dp),
            text = searchedContent,
            maxLines = 2,
            color = ColorBox.text.copy(0.7f),
            textAlign = TextAlign.Center,
            fontSize = 8.sp
        )
    }

}