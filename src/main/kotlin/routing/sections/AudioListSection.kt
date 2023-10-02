package routing.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.MyIconButton
import components.PlayAnimationView
import components.SmoothImage
import components.menu.AudioListPopup
import core.CorePlayer
import core.db.CoreDB
import core.db.models.ModelAudio
import routing.dialogs.AudioInfoDialog
import routing.dialogs.EditTagsDialog
import ru.alexgladkov.odyssey.compose.extensions.present
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.compose.navigation.modal_navigation.AlertConfiguration
import theme.ColorBox
import utils.Global
import utils.Tools.formatToDuration

@Composable
fun AudioListSection(
    visiblePlayer: Boolean,
    showAlbumName: Boolean,
    isPlaying: Boolean,
    maxWidth: Int,
    playingMediaItem: ModelAudio
) {

    val modalController = LocalRootController.current.findModalController()
    var showListPopup by remember { mutableStateOf(Pair(false, ModelAudio())) }

    if (Global.Data.filteredAudioList.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(100.dp),
                painter = painterResource("icons/audio-album.svg"),
                contentDescription = null,
                tint = ColorBox.text.copy(0.8f)
            )
            Spacer(Modifier.padding(10.dp))
            Text(
                text = "No audio item found !!!",
                color = ColorBox.text.copy(0.9f)
            )

        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 6.dp,
                end = 6.dp,
                top = (Global.APPBAR_HEIGHT + 6).dp,
                bottom = if (visiblePlayer && maxWidth < Global.SIZE_NORMAL) 80.dp else 6.dp
            )
        ) {
            items(Global.Data.filteredAudioList) {
                AudioRow(it, playingMediaItem, showAlbumName, isPlaying, visiblePlayer) {
                    showListPopup = Pair(true, it)
                }
            }
        }
    }

    AudioListPopup(
        show = showListPopup,
        onDismissRequest = {
            showListPopup = Pair(false, ModelAudio())
        }
    ) { index, modelAudio ->
        showListPopup = Pair(false, modelAudio)
        when (index) {
            0 -> {
                CoreDB.Audios.removeFromFav(modelAudio.hash)
                Global.Data.addOrRemoveFavorite(modelAudio.hash, false)
            }
            1 -> {
                CoreDB.Audios.addToFav(modelAudio.hash)
                Global.Data.addOrRemoveFavorite(modelAudio.hash, true)
            }
            2 -> {
                //edit
                modalController.present(AlertConfiguration(alpha = 0.6f, cornerRadius = 6)) {
                    EditTagsDialog(
                        modelAudio = modelAudio.copy(),
                        closeClicked = {
                            modalController.popBackStack(null)
                        },
                        onEdited = { editedAudio ->
                            if (playingMediaItem.id == editedAudio.id) {
                                val currentPos = CorePlayer.progressCallback.value
                                CorePlayer.currentMediaItemCallback.value = editedAudio
                                CorePlayer.startPlay(editedAudio, from = currentPos)
                            }
                            Global.Data.editedAudio(editedAudio)
                            modalController.popBackStack(null)
                        }
                    )
                }
            }

            3 -> {
                //info
                modalController.present(AlertConfiguration(alpha = 0.6f, cornerRadius = 6)) {
                    AudioInfoDialog(modelAudio) {
                        modalController.popBackStack(null)
                    }
                }
            }
        }
    }
}

@Composable
private fun AudioRow(
    modelAudio: ModelAudio,
    playingAudio: ModelAudio,
    showAlbumName: Boolean,
    isPlaying: Boolean,
    active: Boolean,
    onMoreClick: () -> Unit
) {

    val play = (isPlaying && (playingAudio.hash == modelAudio.hash) && active)

    val animColorState =
        animateFloatAsState(if ((playingAudio.hash == modelAudio.hash) && active) 1f else 0f, animationSpec = tween(400))
    val backgroundColor = lerp(ColorBox.card, ColorBox.text, animColorState.value)
    val textColor = lerp(ColorBox.text, ColorBox.card, animColorState.value)


    Row(
        modifier = Modifier.padding(6.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp))
            .shadow(3.dp, shape = RoundedCornerShape(16.dp)).background(backgroundColor)
            .clickable {
                CorePlayer.startPlay(modelAudio)
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(48.dp)
                .clip(RoundedCornerShape(50))
                .background(ColorBox.primaryDark)
                .clickable {
                    if (playingAudio.hash == modelAudio.hash) {
                        CorePlayer.autoPlayPause()
                    } else {
                        CorePlayer.startPlay(modelAudio)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            SmoothImage(
                modifier = Modifier.fillMaxSize().alpha(0.5f),
                placeHolder = null,
                image = modelAudio.coverArt,
                contentScale = ContentScale.Crop
            )
            PlayAnimationView(
                modifier = Modifier.size(38.dp),
                color = ColorBox.text,
                play = play
            )
        }
        Column(
            Modifier.padding(start = 8.dp, end = 12.dp).height(64.dp).weight(2f).fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = modelAudio.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = textColor,
                fontSize = 14.sp
            )
            Spacer(Modifier.padding(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = modelAudio.artist,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = textColor.copy(0.8f),
                    fontSize = 12.sp
                )
                AnimatedVisibility(
                    visible = modelAudio.isFav,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Icon(
                        modifier = Modifier.padding(start = 6.dp).size(13.dp),
                        painter = painterResource("icons/heart-bold.svg"),
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            }
        }
        if (showAlbumName) {
            Text(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                text = modelAudio.album,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = textColor.copy(0.7f),
                fontSize = 13.sp
            )
        }
        Text(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp),
            text = modelAudio.duration.formatToDuration(),
            maxLines = 1,
            color = textColor.copy(0.9f),
            fontSize = 13.sp
        )
        MyIconButton(
            icon = "icons/more.svg",
            colorFilter = textColor.copy(0.8f)
        ) {
            onMoreClick.invoke()
        }
    }

}