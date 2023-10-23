package routing.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.db.CoreDB
import core.db.models.ModelDropFiles
import core.extractor.AudioInfo
import core.extractor.AudioStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import theme.ColorBox
import utils.FileUtils.md5
import utils.Global
import java.io.File
import java.net.URLDecoder

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DropFilesDialog(
    onDragState: (Global.DragState) -> Unit,
    onClose : () -> Unit
) {

    var dragState by remember { mutableStateOf(Global.DragState.DRAG_EXIT) }

    val animateFraction = animateFloatAsState(if (dragState == Global.DragState.DRAG_ENTER) 1f else 0f)
    val backgroundColor = lerp(ColorBox.text.copy(0.1f), ColorBox.text.copy(0.3f), animateFraction.value)

    var preparing by remember { mutableStateOf(false) }
    var droppedList by remember { mutableStateOf(listOf<String>()) }
    val preparedList = remember { mutableStateListOf<ModelDropFiles>() }

    LaunchedEffect(dragState) {
        if (dragState == Global.DragState.DROPPED) {
            //preparing dropped files
            preparing = true
            CoroutineScope(Dispatchers.IO).launch {
                droppedList.forEach {
                    val file = File(it)
                    val extension = file.extension
                    if (!AudioStore.supportedFormats.contains(extension)) {
                        preparedList.add(
                            ModelDropFiles(
                                if (file.isDirectory) "dir" else extension,
                                file.name,
                                ModelDropFiles.STATE_NOT_SUPPORT,
                                Color(0xFFef6950)
                            )
                        )
                    } else {
                        val md5 = file.md5()
                        if (CoreDB.Audios.checkAudioExist(md5) != 0) {
                            preparedList.add(
                                ModelDropFiles(extension, file.name, ModelDropFiles.STATE_AVAILABLE, Color(0xFF3399ff))
                            )
                        } else {
                            val modelAudio = AudioInfo.getInfo(file)
                            if (modelAudio != null && modelAudio.duration > 9000) {
                                modelAudio.hash = md5
                                CoreDB.Audios.insert(modelAudio)
                                preparedList.add(
                                    ModelDropFiles(extension, file.name, ModelDropFiles.STATE_ADDED, Color(0xFF00cc00))
                                )
                            } else {
                                preparedList.add(
                                    ModelDropFiles(
                                        extension,
                                        file.name,
                                        ModelDropFiles.STATE_NOT_SUPPORT,
                                        Color(0xFFef6950)
                                    )
                                )
                            }
                        }
                    }
                    preparing = false
                }
            }
        }
    }

    Column(
        modifier = Modifier.width(370.dp).heightIn(max = 570.dp).clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .onExternalDrag(
                enabled = dragState != Global.DragState.DROPPED,
                onDragStart = {
                    dragState = Global.DragState.DRAG_ENTER
                    onDragState.invoke(Global.DragState.DRAG_ENTER)
                },
                onDragExit = {
                    dragState = Global.DragState.DRAG_EXIT
                    onDragState.invoke(Global.DragState.DRAG_EXIT)
                },
                onDrop = { dropData ->
                    dragState = Global.DragState.DROPPED
                    onDragState.invoke(Global.DragState.DROPPED)
                    if (dropData.dragData is DragData.FilesList) {
                        droppedList = (dropData.dragData as DragData.FilesList).readFiles()
                            .map { URLDecoder.decode(it.replace("file:",""), "UTF-8") }
                    }
                }
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.padding(top = 25.dp).size(80.dp),
            painter = painterResource("icons/folder-open.svg"),
            contentDescription = null,
            tint = ColorBox.primary
        )
        Text(
            modifier = Modifier.padding(top = 20.dp, bottom = 25.dp),
            text = if (preparing) "Preparing files" else if (dragState == Global.DragState.DROPPED) "Files that you dropped" else "Drop your audios here",
            color = ColorBox.text.copy(0.8f),
            fontSize = 16.sp
        )
        if (preparing) {
            CircularProgressIndicator(
                Modifier.padding(bottom = 20.dp).size(30.dp),
                strokeWidth = 2.dp,
                strokeCap = StrokeCap.Round
            )
        }
        AnimatedVisibility(
            visible = !preparing && dragState == Global.DragState.DROPPED
        ) {
            Column(Modifier.fillMaxSize()) {
                LazyColumn(Modifier.fillMaxWidth().weight(1f)) {
                    item {
                        Divider(Modifier.fillMaxWidth(), color = ColorBox.text.copy(0.1f))
                    }
                    items(preparedList) {
                        DropItem(it)
                        Divider(Modifier.fillMaxWidth(), color = ColorBox.text.copy(0.1f))
                    }
                }
                Row(
                    Modifier.padding(20.dp).height(40.dp).fillMaxWidth().clip(RoundedCornerShape(50))
                        .background(ColorBox.primary.copy(0.1f)).clickable {
                            if (preparedList.any { it.state == ModelDropFiles.STATE_ADDED }) {
                                Global.Data.reFetchMainList()
                            }
                            onClose.invoke()
                        }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Done work",
                        color = ColorBox.primary
                    )
                }
            }
        }
    }

}

@Composable
private fun DropItem(modelDropFiles: ModelDropFiles) {
    Row(
        Modifier.padding(start = 6.dp, end = 6.dp).fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.clip(RoundedCornerShape(8.dp)).background(modelDropFiles.color.copy(0.1f))) {
            Text(
                modifier = Modifier.padding(5.dp),
                text = modelDropFiles.extension,
                color = modelDropFiles.color,
                fontSize = 11.sp
            )
        }
        Column(Modifier.padding(start = 12.dp, end = 12.dp).weight(1f)) {
            Text(
                text = modelDropFiles.name,
                color = ColorBox.text.copy(0.9f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
            val state = when (modelDropFiles.state) {
                ModelDropFiles.STATE_NOT_SUPPORT -> "File not supported"
                ModelDropFiles.STATE_ADDED -> "Audio added"
                else -> "Audio exist"
            }
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = state,
                color = ColorBox.text.copy(0.7f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 11.sp
            )
        }
        val icon = when (modelDropFiles.state) {
            ModelDropFiles.STATE_NOT_SUPPORT -> "icons/close.svg"
            ModelDropFiles.STATE_ADDED -> "icons/check.svg"
            else -> "icons/arrange-circle.svg"
        }
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = modelDropFiles.color
        )
    }
}