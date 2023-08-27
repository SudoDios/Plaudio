package routing.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.CustomTextField
import components.MyIconButton
import components.SmoothImage
import core.db.CoreDB
import core.db.models.ModelAudio
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.images.ArtworkFactory
import theme.ColorBox
import utils.Tools
import java.io.File

@Composable
fun EditTagsDialog(
    modelAudio: ModelAudio,
    closeClicked: () -> Unit,
    onEdited : (ModelAudio) -> Unit
) {

    val audioFile = AudioFileIO.read(File(modelAudio.path))

    var title by remember { mutableStateOf(modelAudio.name) }
    var artist by remember { mutableStateOf(modelAudio.artist) }
    var cover by remember { mutableStateOf(modelAudio.coverArt) }
    var isChangedCover by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.width(370.dp).background(color = ColorBox.primaryDark2),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(56.dp).background(color = ColorBox.primaryDark.copy(0.5f)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.padding(3.dp))
            MyIconButton(
                contentPadding = 10.dp,
                colorFilter = ColorBox.text.copy(0.6f),
                icon = "icons/close.svg",
                onClick = {
                    closeClicked.invoke()
                }
            )
            Text(
                modifier = Modifier.padding(start = 12.dp).weight(1f),
                text = "Edit Tags",
                color = ColorBox.text.copy(0.8f),
                fontSize = 18.sp
            )
            Row(
                modifier = Modifier.padding(end = 8.dp).height(40.dp).clip(RoundedCornerShape(50))
                    .background(ColorBox.text.copy(0.1f))
                    .clickable {
                        //save changes
                        if (isChangedCover) {
                            audioFile.tag.setField(ArtworkFactory.createArtworkFromFile(File(cover!!)))
                            cover = Tools.writeThumbImage(File(cover!!).readBytes(),cover!!.substringAfterLast("."))
                        }
                        audioFile.tag.setField(FieldKey.TITLE,title)
                        audioFile.tag.setField(FieldKey.ARTIST,artist)
                        AudioFileIO.write(audioFile)
                        val newSize = File(modelAudio.path).length()
                        modelAudio.name = title
                        modelAudio.artist = artist
                        modelAudio.coverArt = cover
                        modelAudio.size = newSize
                        CoreDB.Audios.update(modelAudio)
                        onEdited.invoke(modelAudio)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp).size(22.dp),
                    painter = painterResource("icons/check.svg"),
                    contentDescription = null,
                    tint = ColorBox.text
                )
                Text(
                    modifier = Modifier.padding(end = 10.dp),
                    text = "Save changes",
                    fontSize = 12.sp,
                    color = ColorBox.text
                )
            }
        }
        Spacer(Modifier.padding(6.dp))
        EditCoverView(cover) {
            cover = it
            isChangedCover = true
        }
        Spacer(Modifier.padding(6.dp))
        CustomTextField(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp).fillMaxWidth(),
            value = title,
            onValueChange = { title = it },
            singleLine = true,
            label = { Text("Title") }
        )
        CustomTextField(
            modifier = Modifier.padding(top = 8.dp, start = 20.dp, end = 20.dp, bottom = 20.dp).fillMaxWidth(),
            value = artist,
            singleLine = true,
            onValueChange = { artist = it },
            label = { Text("Artist") }
        )
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun EditCoverView(cover: String?, onCoverSelected: (String) -> Unit) {

    var onMouseHover by remember { mutableStateOf(false) }

    Box(modifier = Modifier.size(120.dp)
        .onPointerEvent(PointerEventType.Enter) {
            onMouseHover = true
        }
        .onPointerEvent(PointerEventType.Exit) {
            onMouseHover = false
        }.clip(RoundedCornerShape(16.dp)).background(ColorBox.primaryDark)
    )
    {
        SmoothImage(modifier = Modifier.fillMaxSize(), image = cover, contentScale = ContentScale.Crop)
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = onMouseHover,
            enter = slideInVertically(animationSpec = tween(120), initialOffsetY = {
                it
            }),
            exit = slideOutVertically(animationSpec = tween(120), targetOffsetY = {
                it
            })
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(ColorBox.primaryDark.copy(0.5f))
                    .clickable {
                        val imageSel = Tools.openFilePicker(title = "Choice Image", "jpg", "jpeg", "png")
                        if (imageSel != null) {
                            onCoverSelected.invoke(imageSel.absolutePath)
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.size(22.dp),
                    painter = painterResource("icons/edit-image.svg"),
                    contentDescription = null,
                    tint = ColorBox.text
                )
            }
        }
    }
}