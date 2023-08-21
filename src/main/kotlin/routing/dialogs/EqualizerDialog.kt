package routing.dialogs

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.AnimatedText
import components.MyIconButton
import core.CorePlayer
import theme.ColorBox
import utils.Prefs
import utils.Tools.formatToCount
import kotlin.math.pow

@Composable
fun EqualizerDialog(closeClicked : () -> Unit) {

    val equalizerList = CorePlayer.getEqualizerList()

    var switchTurnOnOff by remember { mutableStateOf(Prefs.equalizerOn) }
    val disableAnimFraction = animateFloatAsState(if (!switchTurnOnOff) 0.6f else 0f, animationSpec = tween(400))

    var isOpenPresets by remember { mutableStateOf(false) }
    var currentPresetName by remember { mutableStateOf(Prefs.equalizerPreset) }

    val amps = equalizerList[currentPresetName]!!

    Column(modifier = Modifier.width(370.dp).background(ColorBox.primaryDark2), horizontalAlignment = Alignment.CenterHorizontally) {
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
                modifier = Modifier.padding(start = 12.dp),
                text = "Equalizer",
                color = ColorBox.text.copy(0.8f),
                fontSize = 18.sp
            )
        }
        Spacer(Modifier.padding(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp).clip(
                RoundedCornerShape(50)
            ).background(ColorBox.text.copy(0.1f)).clickable {
                switchTurnOnOff = !switchTurnOnOff
                if (switchTurnOnOff) {
                    CorePlayer.turnOnEqualizer(currentPresetName)
                } else {
                    CorePlayer.turnOffEqualizer()
                }
            }) {
            Text(
                "Turn On / Off",
                modifier = Modifier.weight(1f).padding(start = 12.dp),
                color = ColorBox.text.copy(0.8f),
                fontSize = 14.sp
            )
            Switch(
                modifier = Modifier.padding(end = 6.dp),
                checked = switchTurnOnOff,
                onCheckedChange = {
                    switchTurnOnOff = it
                    if (switchTurnOnOff) {
                        CorePlayer.turnOnEqualizer(currentPresetName)
                    } else {
                        CorePlayer.turnOffEqualizer()
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ColorBox.primary, uncheckedThumbColor = ColorBox.primaryDark2
                )
            )
        }
        Spacer(Modifier.padding(10.dp))
        Box(Modifier.fillMaxWidth().drawWithContent {
            drawContent()
            drawRect(ColorBox.primaryDark2.copy(disableAnimFraction.value))
        }) {
            Column(Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Preset",
                        fontSize = 13.sp,
                        color = ColorBox.text.copy(0.6f)
                    )
                    Row(
                        modifier = Modifier.padding(start = 16.dp).height(42.dp).weight(1f).clip(RoundedCornerShape(50))
                            .background(ColorBox.text.copy(0.1f)).clickable(enabled = switchTurnOnOff) {
                                isOpenPresets = true
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedText(
                            modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                            text = currentPresetName,
                            fontSize = 14.sp,
                            color = ColorBox.text
                        )
                        Spacer(Modifier.weight(1f))
                        Icon(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource("icons/arrow-down.svg"),
                            contentDescription = null,
                            tint = ColorBox.text.copy(0.8f)
                        )
                        Spacer(Modifier.padding(end = 12.dp))
                    }
                }
                listFreq(amps)
                Spacer(Modifier.padding(10.dp))
                PresetsPopup(
                    open = isOpenPresets,
                    presets = equalizerList,
                    onDismissRequest = {
                        isOpenPresets = false
                    },
                    onClick = { name ->
                        isOpenPresets = false
                        currentPresetName = name
                        CorePlayer.turnOnEqualizer(name)
                    }
                )
            }
        }
    }

}

@Composable
private fun PresetsPopup(
    open: Boolean = false,
    onDismissRequest: () -> Unit,
    presets: Map<String, FloatArray>,
    onClick: (String) -> Unit
) {
    CursorDropdownMenu(
        expanded = open,
        onDismissRequest = onDismissRequest
    ) {
        for (i in presets.keys) {
            Row(
                modifier = Modifier.height(40.dp).fillMaxWidth().clickable {
                    onClick.invoke(i)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.padding(6.dp))
                Text(modifier = Modifier.padding(end = 16.dp), text = i, color = ColorBox.text, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun listFreq(amps: FloatArray) {
    Row(modifier = Modifier.padding(start = 12.dp, end = 12.dp).fillMaxWidth()) {
        for (it in amps.indices) {
            val amp = amps[it]
            verticalAmp(
                mod = Modifier.weight(1f),
                modifier = Modifier.padding(top = 20.dp, bottom = 12.dp).width(4.dp).height(190.dp),
                value = amp,
                getFreqText(it)
            )
        }
    }
}


@Composable
private fun verticalAmp(mod: Modifier, modifier: Modifier, value: Float, freq: String) {

    val calcOffsetY = (100f - (((value + 20) * 100) / 40f)) / 100f

    val animate = animateFloatAsState(calcOffsetY)

    Column(modifier = mod, horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(modifier) {

            val height = size.height
            val width = size.width

            drawIntoCanvas {
                //background
                drawRoundRect(
                    color = ColorBox.text,
                    alpha = 0.1f,
                    topLeft = Offset(0f, 0f),
                    size = Size(width, height),
                    cornerRadius = CornerRadius(2f, 2f)
                )
                //foreground
                drawRoundRect(
                    color = ColorBox.text,
                    topLeft = Offset(0f, animate.value * height),
                    size = Size(width, (1f - animate.value) * height),
                    cornerRadius = CornerRadius(2f, 2f)
                )
            }

        }

        Text(
            text = freq,
            fontSize = 12.sp,
            color = ColorBox.text.copy(0.3f)
        )
    }

}


private fun getFreqText(index: Int): String {
    return 2.0.pow(index + 5).toLong().formatToCount()
}