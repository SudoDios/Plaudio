package routing.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.MyIconButton
import core.CorePlayer
import routing.sections.EqualizerLayout
import theme.ColorBox
import utils.Prefs

@Composable
fun EqualizerDialog(closeClicked : () -> Unit) {

    var switchTurnOnOff by remember { mutableStateOf(Prefs.equalizerOn) }
    var currentPresetName by remember { mutableStateOf(Prefs.equalizerPreset) }

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
                modifier = Modifier.padding(start = 12.dp).weight(1f),
                text = "Equalizer",
                color = ColorBox.text.copy(0.8f),
                fontSize = 18.sp
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
                    checkedThumbColor = ColorBox.primary,
                    uncheckedThumbColor = ColorBox.primary.copy(0.6f),
                    uncheckedTrackColor = ColorBox.primary,
                    uncheckedTrackAlpha = 0.1f
                )
            )
        }
        Spacer(Modifier.padding(10.dp))
        EqualizerLayout(
            switchTurnOnOff,
            currentPresetName,
            switchedPreset = {
                currentPresetName = it
            }
        )
    }

}