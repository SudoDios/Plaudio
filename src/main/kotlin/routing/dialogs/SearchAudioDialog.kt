package routing.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.AnimatedText
import components.MyIconButton
import components.SSDScanAnimation
import core.db.models.ModelAudio
import core.extractor.AudioStore
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import theme.ColorBox
import utils.Global
import utils.Prefs

@Composable
fun SearchAudioDialog(onFinished: () -> Unit) {

    val rootController = LocalRootController.current
    val modalController = rootController.findModalController()

    var folderCount by remember { mutableStateOf(0) }
    var audioCount by remember { mutableStateOf(0) }
    var albumCount by remember { mutableStateOf(0) }
    var artistCount by remember { mutableStateOf(0) }

    var searchFolderCount by remember { mutableStateOf(0) }
    var isSearching by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        AudioStore.callback = object : AudioStore.Callback {
            override fun onCounting(audios: ArrayList<ModelAudio>) {
                folderCount = audios.distinctBy { it.folder }.size
                audioCount = audios.size
                albumCount = audios.distinctBy { it.album }.size
                artistCount = audios.distinctBy { it.artist }.size
            }
            override fun onFinished() {
                onFinished.invoke()
                Prefs.isFirstInitialized = true
            }
            override fun onCountingFolder(count: Int) {
                searchFolderCount = count
            }
        }
    }

    Column(
        modifier = Modifier.width(400.dp).background(ColorBox.primaryDark2),
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
                    if (!isSearching) {
                        modalController.popBackStack(null)
                    }
                }
            )
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Sync Settings",
                color = ColorBox.text.copy(0.8f),
                fontSize = 18.sp
            )
        }
        SSDScanAnimation(
            isSearching = isSearching,
            Modifier.padding(top = 30.dp, bottom = 12.dp).size(90.dp)
        )
        Row(
            Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(ColorBox.primary.copy(0.1f))
            , verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f).padding(12.dp)) {
                Text(
                    text = "Target Folder",
                    color = ColorBox.text.copy(0.9f),
                    fontSize = 13.sp
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = Global.userHome,
                    color = ColorBox.text.copy(0.7f),
                    fontSize = 12.sp
                )
            }
        }
        AnimatedVisibility(
            visible = isSearching
        ) {
            Column(modifier = Modifier.padding(bottom = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Searched $searchFolderCount folders",
                    color = ColorBox.text.copy(0.8f),
                    fontSize = 13.sp
                )
                Row(Modifier.padding(top = 16.dp, start = 20.dp, end = 20.dp).fillMaxWidth()) {
                    FindItem(
                        icon = "icons/folder.svg",
                        key = "Folders",
                        value = folderCount.toString()
                    )
                    Spacer(Modifier.padding(5.dp))
                    FindItem(
                        icon = "icons/music-note.svg",
                        key = "Audios",
                        value = audioCount.toString()
                    )
                }
                Row(Modifier.padding(top = 16.dp, start = 20.dp, end = 20.dp).fillMaxWidth()) {
                    FindItem(
                        icon = "icons/audio-album.svg",
                        key = "Albums",
                        value = albumCount.toString()
                    )
                    Spacer(Modifier.padding(5.dp))
                    FindItem(
                        icon = "icons/mic.svg",
                        key = "Artists",
                        value = artistCount.toString()
                    )
                }
            }
        }
        Box(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                .fillMaxWidth().height(36.dp)
                .alpha(if (isSearching) 0.5f else 1f)
                .clip(RoundedCornerShape(50))
                .background(ColorBox.primary)
                .clickable(
                    enabled = !isSearching,
                    indication = rememberRipple(
                        color = ColorBox.primaryDark
                    ),
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    isSearching = !isSearching
                    AudioStore.findAudios(Global.userHome)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Start sync device files",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                letterSpacing = 1.25.sp,
                color = ColorBox.primaryDark
            )
        }
    }

}


@Composable
fun RowScope.FindItem(
    icon: String,
    value: String,
    key: String
) {
    Row(
        Modifier.weight(1f).clip(RoundedCornerShape(50)).background(ColorBox.text.copy(0.1f)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(12.dp).size(22.dp),
            painter = painterResource(icon),
            contentDescription = null,
            tint = ColorBox.text.copy(0.8f)
        )
        AnimatedText(
            modifier = Modifier,
            text = value,
            color = ColorBox.text,
            fontSize = 14.sp
        )
        Spacer(Modifier.padding(3.dp))
        Text(
            text = key,
            color = ColorBox.text.copy(0.8f),
            fontSize = 13.sp
        )
    }
}