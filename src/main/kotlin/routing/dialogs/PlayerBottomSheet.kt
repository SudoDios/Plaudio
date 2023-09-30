package routing.dialogs

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.*
import components.menu.RepeatModePopup
import components.menu.SpeedPlaybackPopup
import core.CorePlayer
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import ru.alexgladkov.odyssey.compose.extensions.present
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.compose.navigation.modal_navigation.AlertConfiguration
import theme.ColorBox
import utils.Prefs
import utils.Tools
import utils.Tools.formatToDuration
import utils.Tools.roundPlace

@Composable
fun PlayerBottomSheet(
    isOnBoard : Boolean= false,
    onClose: () -> Unit
) {

    var showRepeatPopup by remember { mutableStateOf(false) }

    var showSpeedPopup by remember { mutableStateOf(false) }
    var speedChange by remember { mutableStateOf("${((Prefs.playbackSpeed * 1.5) + 0.5).roundPlace(1)}x") }

    RepeatModePopup(
        show = showRepeatPopup,
        onDismissRequest = {
            showRepeatPopup = false
        },
        callback = {
            showRepeatPopup = false
        }
    )
    SpeedPlaybackPopup(
        show = showSpeedPopup,
        onDismissRequest = {
            showSpeedPopup = false
        },
        callback = {
            speedChange = it
        }
    )


    val isActive = CorePlayer.visiblePlayer.observeAsState()


    val baseModifier = if (isOnBoard) {
        Modifier.padding(12.dp).width(400.dp).clip(RoundedCornerShape(16.dp)).background(ColorBox.primaryDark2)
    } else {
        Modifier.width(400.dp)
    }

    Box(modifier = baseModifier) {
        if (isOnBoard) {
            AnimatedVisibility(
                visible = isActive.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Content(true, onClose, speedChange,
                    repeatClicked = {
                        showRepeatPopup = true
                    },
                    speedClicked = {
                        showSpeedPopup = true
                    }
                )
            }
            if (!isActive.value) {
                Column(Modifier.align(Alignment.Center).height(562.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        modifier = Modifier.size(60.dp),
                        painter = painterResource("icons/play-circle.svg"),
                        contentDescription = null,
                        tint = ColorBox.text.copy(0.8f)
                    )
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = "No item now playing",
                        color = ColorBox.text.copy(0.8f),
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            Content(false, onClose, speedChange,
                repeatClicked = {
                    showRepeatPopup = true
                },
                speedClicked = {
                    showSpeedPopup = true
                }
            )
        }
    }

}

@Composable
private fun Content (
    isOnBoard: Boolean,
    onClose: () -> Unit,
    speedChange: String,
    repeatClicked: () -> Unit,
    speedClicked: () -> Unit
) {

    val modalController = LocalRootController.current.findModalController()
    val repeatModeChange = Prefs.repeatModeChanged.observeAsState()

    val isMute = CorePlayer.isMutedCallback.observeAsState()
    val volumeChanged = CorePlayer.volumeChangedCallback.observeAsState()
    val mediaItem = CorePlayer.currentMediaItemCallback.observeAsState()

    val isPlaying = CorePlayer.playPauseCallback.observeAsState()
    val isEqEnable = CorePlayer.isEqEnableCallback.observeAsState()

    var backgroundImageHeight by remember { mutableStateOf(0) }

    SmoothImage(
        modifier = Modifier.fillMaxWidth().height(backgroundImageHeight.dp).blur(20.dp).alpha(0.6f),
        image = mediaItem.value.coverArt,
        fadeOnChange = true,
        contentScale = ContentScale.Crop
    )
    Column(Modifier.fillMaxWidth().onSizeChanged { backgroundImageHeight = it.height }, horizontalAlignment = Alignment.CenterHorizontally) {
        if (!isOnBoard) {
            Icon(
                modifier = Modifier.height(38.dp).fillMaxWidth().clickable {
                    onClose.invoke()
                },
                painter = painterResource("icons/arrow-down.svg"),
                contentDescription = null,
                tint = ColorBox.text.copy(0.7f)
            )
        } else {
            Spacer(Modifier.padding(top = 20.dp))
        }
        Spacer(Modifier.padding(top = 12.dp))
        SmoothImage(
            modifier = Modifier.size(if (!isOnBoard) 230.dp else 240.dp).aspectRatio(1f).clip(RoundedCornerShape(16.dp)),
            fadeOnChange = true,
            image = mediaItem.value.coverArt,
            contentScale = ContentScale.Crop
        )
        AnimatedContent(
            targetState = mediaItem.value.name,
            transitionSpec = {
                (slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()).using(SizeTransform(false))
            }
        ) { audioName ->
            Text(
                modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp).fillMaxWidth(),
                text = audioName,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = ColorBox.text.copy(0.9f),
                textAlign = TextAlign.Center,
                fontSize = 15.sp
            )
        }
        Spacer(Modifier.padding(6.dp))
        Text(
            text = mediaItem.value.artist,
            color = ColorBox.text.copy(0.8f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 13.sp
        )
        DurationLayout(mediaItem.value.duration)
        Row(
            modifier = Modifier.padding(start = 20.dp,bottom = 4.dp,end = 20.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            MyIconButton(
                size = 40.dp,
                icon = Tools.getRepeatModeIconByType(repeatModeChange.value),
                background = ColorBox.text.copy(0.1f),
                colorFilter = ColorBox.text,
                contentPadding = 10.dp,
                onClick = {
                    repeatClicked.invoke()
                }
            )
            MyIconButton(
                size = 44.dp,
                icon = "icons/skip-previous.svg",
                background = ColorBox.text.copy(0.1f),
                colorFilter = ColorBox.text,
                contentPadding = 13.dp,
                onClick = {
                    CorePlayer.previous()
                }
            )
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(50))
                    .background(ColorBox.text).clickable {
                        CorePlayer.autoPlayPause()
                    }, contentAlignment = Alignment.Center
            ) {
                PlayAnimationView(
                    modifier = Modifier.size(44.dp),
                    color = ColorBox.primaryDark,
                    isPlaying.value
                )
            }
            MyIconButton(
                size = 44.dp,
                icon = "icons/skip-next.svg",
                background = ColorBox.text.copy(0.1f),
                colorFilter = ColorBox.text,
                contentPadding = 13.dp,
                onClick = {
                    CorePlayer.next()
                }
            )
            Box(
                modifier = Modifier.size(40.dp)
                    .clip(RoundedCornerShape(50))
                    .background(ColorBox.text.copy(0.1f))
                    .clickable(
                        role = Role.Button,
                        onClick = {
                            speedClicked.invoke()
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = speedChange,
                    color = ColorBox.text,
                    fontSize = 11.sp
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.padding(20.dp).weight(1f).height(48.dp).clip(RoundedCornerShape(50))
                    .background(ColorBox.text.copy(0.1f)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.size(48.dp).clip(RoundedCornerShape(50)).clickable {
                    CorePlayer.autoMute()
                }) {
                    MuteAnimationIcon(modifier = Modifier.size(26.dp).align(Alignment.Center),color = ColorBox.text.copy(0.8f), isMute = isMute.value)
                }
                CustomSlider(
                    modifier = Modifier.padding(start = 4.dp,end = 16.dp).weight(1f),
                    value = (volumeChanged.value / 1.2f / 100f),
                    trackColor = ColorBox.text,
                    thumbColor = ColorBox.text,
                    thumbSize = 6.dp,
                    trackHeight = 2.6.dp,
                    valueChanged = {
                        CorePlayer.changeVolume((it * 1.2 * 100).toInt())
                    },
                    valueChangedFinished = {}
                )
                Text(
                    text = "${volumeChanged.value}%",
                    color = ColorBox.text.copy(0.8f),
                    fontSize = 12.sp
                )
                Spacer(Modifier.padding(6.dp))
            }
            Box(Modifier.padding(end = 20.dp).size(48.dp)) {
                MyIconButton(
                    background = ColorBox.text.copy(0.1f),
                    icon = "icons/equalizer.svg",
                    colorFilter = ColorBox.text.copy(0.8f),
                    onClick = {
                        modalController.present(AlertConfiguration(alpha = 0.6f, cornerRadius = 6)) {
                            EqualizerDialog {
                                modalController.popBackStack(null)
                            }
                        }
                    }
                )
                this@Row.AnimatedVisibility(
                    modifier = Modifier.align(Alignment.TopEnd),
                    visible = isEqEnable.value,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    CircleDot(Modifier.padding(3.dp).size(8.dp), color = Color.Green)
                }
            }
        }
    }
}

@Composable
private fun DurationLayout (duration : Long) {

    var progressSeekValue by remember { mutableStateOf(0f) }
    var isSeeking by remember { mutableStateOf(false) }

    val currentProgress = CorePlayer.progressCallback.observeAsState()

    Row(
        modifier = Modifier.padding(start = 25.dp, end = 25.dp, top = 16.dp, bottom = 16.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        val pos = (currentProgress.value * duration).toLong().formatToDuration()
        val dur = duration.formatToDuration()
        Text(
            text = pos,
            color = ColorBox.text.copy(0.7f),
            fontSize = 11.sp
        )
        CustomSlider(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp).weight(1f),
            value = if (isSeeking) progressSeekValue else currentProgress.value,
            trackColor = ColorBox.text,
            thumbColor = ColorBox.text,
            thumbSize = 8.dp,
            alwaysShowThumb = true,
            valueChanged = {
                progressSeekValue = it
                isSeeking = true
            },
            valueChangedFinished = {
                isSeeking = false
                CorePlayer.seekTo(progressSeekValue)
            }
        )
        Text(
            text = dur,
            color = ColorBox.text.copy(0.7f),
            fontSize = 11.sp
        )
    }
}