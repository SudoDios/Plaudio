package routing.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import components.CustomOcState
import core.CorePlayer
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import routing.dialogs.PlayerBottomSheet
import utils.Global
import utils.areaBlur
import kotlin.math.floor

@Composable
fun BoxWithConstraintsScope.Content(
    drawerState: CustomOcState,
    scope: CoroutineScope
) {

    val visiblePlayer = CorePlayer.visiblePlayer.observeAsState()
    val isPlaying = CorePlayer.playPauseCallback.observeAsState()
    val playingMediaItem = CorePlayer.currentMediaItemCallback.observeAsState()

    if (maxWidth.value > Global.SIZE_LARGE) {
        if (drawerState.isOpen) {
            scope.launch {
                drawerState.close()
            }
        }
    }

    Row(Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = this@Content.maxWidth.value > Global.SIZE_LARGE
        ) {
            Drawer {

            }
        }
        Row(Modifier.fillMaxSize()) {
            Box(Modifier.weight(1f).fillMaxHeight()) {
                BoxWithConstraints(
                    Modifier.fillMaxSize().areaBlur(
                        size = Offset(this@Content.maxWidth.value, Global.APPBAR_HEIGHT.toFloat()),
                        radius = 0f
                    )
                ) {
                    if (Global.Data.searchType.value != Global.Data.SEARCH_TYPE_ALL) {
                        val gridCount = floor(maxWidth.value.toDouble() / 184.0)
                        SearchListSection(
                            visiblePlayer = visiblePlayer.value,
                            this@Content.maxWidth.value.toInt(),
                            gridCount.toInt()
                        )
                    } else {
                        AudioListSection(
                            visiblePlayer.value,
                            showAlbumName = maxWidth > 700.dp,
                            isPlaying.value,
                            this@Content.maxWidth.value.toInt(),
                            playingMediaItem.value
                        )
                    }
                }
                Appbar(
                    this@Content.maxWidth.value < Global.SIZE_LARGE,
                    drawerState,
                    scope
                )
            }
            AnimatedVisibility(
                visible = this@Content.maxWidth.value > Global.SIZE_NORMAL
            ) {
                Column(Modifier.fillMaxHeight()) {
                    PlayerBottomSheet(
                        isOnBoard = true
                    ) {

                    }
                    if (this@Content.maxHeight.value > Global.SIZE_HEIGHT_LARGE) {
                        MostPlaySection(
                            playingMediaItem.value,
                            visiblePlayer.value
                        )
                    }
                }
            }
        }
    }

}

