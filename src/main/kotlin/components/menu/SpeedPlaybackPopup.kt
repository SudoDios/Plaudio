package components.menu

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.CorePlayer
import theme.ColorBox
import utils.Prefs
import utils.Tools.roundPlace

@Composable
fun SpeedPlaybackPopup(
    show: Boolean = false,
    onDismissRequest: () -> Unit,
    callback: (String) -> Unit,
) {

    var speedSeekValue by remember { mutableStateOf(Pair(false,Prefs.playbackSpeed)) }

    CustomDropdownMenu(
        expanded = show,
        onDismissRequest = onDismissRequest
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
            text = "Playback Speed",
            fontSize = 10.sp,
            color = ColorBox.text.copy(0.6f)
        )
        Box(modifier = Modifier.width(170.dp)) {
            SpeedSeekbar(
                modifier = Modifier.fillMaxWidth().height(40.dp),
                value = speedSeekValue,
                valueChanged = {
                    speedSeekValue = Pair(false,it)
                    Prefs.playbackSpeed = speedSeekValue.second
                },
                valueChangedFinished = {
                    CorePlayer.changeSpeed((speedSeekValue.second * 1.5f) + 0.5f)
                    callback.invoke("${((speedSeekValue.second * 1.5) + 0.5).roundPlace(1)}x")
                }
            )
            Text(
                modifier = Modifier.padding(start = 16.dp).align(Alignment.CenterStart),
                text = "${((speedSeekValue.second * 1.5) + 0.5).roundPlace(1)}x",
                color = ColorBox.text,
                fontSize = 13.sp
            )
        }
        Divider(Modifier.padding(top = 6.dp, bottom = 2.dp).fillMaxWidth(), color = ColorBox.text.copy(0.1f))
        val convertSpeedStr = ((speedSeekValue.second * 1.5) + 0.5).roundPlace(1).toString()
        Items("0.5x",isSelected = convertSpeedStr == "0.5") {
            speedSeekValue = Pair(true,0f)
            Prefs.playbackSpeed = 0f
            CorePlayer.changeSpeed(0.5f)
            callback.invoke("0.5x")
        }
        Items("1.0x",isSelected = convertSpeedStr == "1.0") {
            speedSeekValue = Pair(true,0.33f)
            Prefs.playbackSpeed = 0.33f
            CorePlayer.changeSpeed(1f)
            callback.invoke("1.0x")
        }
        Items("1.5x",isSelected = convertSpeedStr == "1.5") {
            speedSeekValue = Pair(true,0.66f)
            Prefs.playbackSpeed = 0.66f
            CorePlayer.changeSpeed(1.5f)
            callback.invoke("1.5x")
        }
        Items("2.0x",isSelected = convertSpeedStr == "2.0") {
            speedSeekValue = Pair(true,1f)
            Prefs.playbackSpeed = 1f
            CorePlayer.changeSpeed(2f)
            callback.invoke("2.0x")
        }
    }

}

@Composable
private fun Items (text : String,isSelected : Boolean,onClick : () -> Unit) {
    val color = if (isSelected) ColorBox.text else ColorBox.text.copy(0.6f)
    Row(modifier = Modifier.height(40.dp).fillMaxWidth().clickable { onClick.invoke() }, verticalAlignment = Alignment.CenterVertically) {
        Spacer(Modifier.padding(6.dp))
        Text(modifier = Modifier.padding(end = 16.dp),text = text, color = color, fontSize = 13.sp)
    }
}

@Composable
private fun SpeedSeekbar(
    modifier: Modifier,
    value: Pair<Boolean,Float> = Pair(false,0f),
    valueChanged: (Float) -> Unit,
    valueChangedFinished: () -> Unit
) {

    var currentWidth = 0

    var isPressed by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(0f) }

    val animateProgress = animateFloatAsState(value.second)

    Canvas(
        modifier = modifier.clip(RoundedCornerShape(3.dp)).onGloballyPositioned {
            currentWidth = it.size.width
        }.draggable(
            orientation = Orientation.Horizontal,
            state = rememberDraggableState { delta ->
                offsetX += delta
                offsetX = offsetX.coerceIn(0f, currentWidth.toFloat())
                val progress = (offsetX / currentWidth.toFloat())
                valueChanged.invoke(progress)
            },
            onDragStopped = {
                isPressed = false
                valueChangedFinished.invoke()
            },
            onDragStarted = {
                offsetX = it.x
                isPressed = true
            },
        )
    ) {
        drawRect(
            color = ColorBox.text.copy(0.2f),
            size = Size(size.width * if (value.first) animateProgress.value else value.second, size.height)
        )
    }

}