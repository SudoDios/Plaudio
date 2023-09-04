package routing.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.*
import components.menu.RepeatModePopup
import core.CorePlayer
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import theme.ColorBox
import utils.Global
import utils.Prefs
import utils.Tools
import utils.Tools.formatToDuration

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoxWithConstraintsScope.CompactPlayer(
    onClicked : () -> Unit,
    onForceHide : () -> Unit
) {

    val modalController = LocalRootController.current.findModalController()

    val visible = CorePlayer.visiblePlayer.observeAsState()
    var showRepeatPopup by remember { mutableStateOf(false) }
    val repeatModeChange = Prefs.repeatModeChanged.observeAsState()

    if (!visible.value) modalController.popBackStack(null)
    if (maxWidth.value.toInt() > Global.SIZE_NORMAL) onForceHide.invoke()

    RepeatModePopup(
        show = showRepeatPopup,
        onDismissRequest = {
            showRepeatPopup = false
        },
        callback = {
            showRepeatPopup = false
        }
    )

    AnimatedVisibility(
        modifier = Modifier.align(Alignment.BottomCenter),
        visible = visible.value && (maxWidth.value.toInt() < Global.SIZE_NORMAL),
        enter = slideInVertically(initialOffsetY = {
            it
        }),
        exit = slideOutVertically(targetOffsetY = {
            it
        })
    ) {

        val mediaItem = CorePlayer.currentMediaItemCallback.observeAsState()
        val isPlaying = CorePlayer.playPauseCallback.observeAsState()
        val currentProgress = CorePlayer.progressCallback.observeAsState()

        var progressSeekValue by remember { mutableStateOf(0f) }
        var isSeeking by remember { mutableStateOf(false) }

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(74.dp)
            .background(ColorBox.primaryDark2)
            .drawBehind {
                val xFraction = if (isSeeking) progressSeekValue else currentProgress.value
                drawRect(
                    color = ColorBox.text.copy(0.1f),
                    size = Size(size.width * xFraction,size.height)
                )
            }
            .clickable {
                onClicked.invoke()
            }
        ) {
            Row(Modifier.padding(top = 2.dp).fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.padding(start = 8.dp))
                SmoothImage(
                    modifier = Modifier.size(56.dp).clip(RoundedCornerShape(14.dp)),
                    fadeOnChange = true,
                    image = mediaItem.value.coverArt,
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier.weight(1f).padding(start = 12.dp,end = 12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    AnimatedText(
                        modifier = Modifier.basicMarquee(),
                        text = mediaItem.value.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = ColorBox.text.copy(0.9f),
                        fontSize = 15.sp
                    )
                    Spacer(Modifier.padding(2.dp))
                    Text(
                        text = mediaItem.value.artist,
                        color = ColorBox.text.copy(0.8f),
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.padding(2.dp))
                    val pos = (currentProgress.value * mediaItem.value.duration).toLong().formatToDuration() + " / "
                    val dur = mediaItem.value.duration.formatToDuration()
                    Text(
                        text = "$pos$dur",
                        color = ColorBox.text.copy(0.6f),
                        fontSize = 11.sp
                    )
                }
                MyIconButton(
                    size = 38.dp,
                    icon = "icons/skip-previous.svg",
                    background = ColorBox.text.copy(0.1f),
                    colorFilter = ColorBox.text,
                    contentPadding = 10.dp,
                    onClick = {
                        CorePlayer.previous()
                    }
                )
                Box(
                    modifier = Modifier
                        .padding(start = 10.dp,end = 10.dp)
                        .size(48.dp)
                        .clip(RoundedCornerShape(50))
                        .background(ColorBox.text.copy(0.1f)).clickable {
                            CorePlayer.autoPlayPause()
                        }, contentAlignment = Alignment.Center
                ) {
                    PlayAnimationView(
                        modifier = Modifier.size(36.dp),
                        color = ColorBox.primary,
                        isPlaying.value
                    )
                }
                MyIconButton(
                    size = 38.dp,
                    padding = PaddingValues(end = 10.dp),
                    icon = "icons/skip-next.svg",
                    background = ColorBox.text.copy(0.1f),
                    colorFilter = ColorBox.text,
                    contentPadding = 10.dp,
                    onClick = {
                        CorePlayer.next()
                    }
                )
                AnimatedVisibility(
                    visible = this@CompactPlayer.maxWidth.value.toInt() > Global.SIZE_SMALL
                ) {
                    MyIconButton(
                        size = 38.dp,
                        padding = PaddingValues(end = 10.dp),
                        icon = Tools.getRepeatModeIconByType(repeatModeChange.value),
                        background = ColorBox.text.copy(0.1f),
                        colorFilter = ColorBox.text,
                        contentPadding = 10.dp,
                        onClick = {
                            showRepeatPopup = true
                        }
                    )
                }
            }
            CustomSlider(
                modifier = Modifier.fillMaxWidth().offset(y = (-10).dp),
                value = if (isSeeking) progressSeekValue else currentProgress.value,
                trackColor = ColorBox.primary,
                thumbColor = ColorBox.primary,
                thumbSize = 7.dp,
                trackHeight = 3.dp,
                valueChanged = {
                    progressSeekValue = it
                    isSeeking = true
                },
                valueChangedFinished = {
                    isSeeking = false
                    CorePlayer.seekTo(progressSeekValue)
                }
            )
        }
    }


}