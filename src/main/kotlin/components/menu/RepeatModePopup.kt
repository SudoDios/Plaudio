package components.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.ColorBox
import utils.Prefs

@Composable
fun RepeatModePopup(
    show : Boolean = false,
    onDismissRequest : () -> Unit,
    callback : (Int) -> Unit
) {

    val currentMode = Prefs.repeatMode

    CustomDropdownMenu(
        expanded = show,
        onDismissRequest = onDismissRequest
    ) {
        MenuItem("icons/repeat-all.svg",isSelected = currentMode == Prefs.REPEAT_MODE_ALL,"Repeat All") {
            Prefs.repeatMode = Prefs.REPEAT_MODE_ALL
            callback.invoke(Prefs.REPEAT_MODE_ALL)
        }
        MenuItem("icons/repeat-one.svg",isSelected = currentMode == Prefs.REPEAT_MODE_ONE,"Repeat One") {
            Prefs.repeatMode = Prefs.REPEAT_MODE_ONE
            callback.invoke(Prefs.REPEAT_MODE_ONE)
        }
        MenuItem("icons/shuffle.svg",isSelected = currentMode == Prefs.REPEAT_MODE_SHUFFLE,"Shuffle") {
            Prefs.repeatMode = Prefs.REPEAT_MODE_SHUFFLE
            callback.invoke(Prefs.REPEAT_MODE_SHUFFLE)
        }
        MenuItem("icons/stop.svg",isSelected = currentMode == Prefs.REPEAT_MODE_STOP,"Stop") {
            Prefs.repeatMode = Prefs.REPEAT_MODE_STOP
            callback.invoke(Prefs.REPEAT_MODE_STOP)
        }
    }

}

@Composable
private fun MenuItem (icon : String,isSelected : Boolean,text : String,onClick : () -> Unit) {
    val color = if (isSelected) ColorBox.text else ColorBox.text.copy(0.6f)
    Row(modifier = Modifier.height(40.dp).fillMaxWidth().widthIn(min = 160.dp).clickable { onClick.invoke() }, verticalAlignment = Alignment.CenterVertically) {
        Spacer(Modifier.padding(6.dp))
        Icon(
            modifier = Modifier.size(22.dp),
            painter = painterResource(icon),
            contentDescription = null,
            tint = color
        )
        Spacer(Modifier.padding(6.dp))
        Text(modifier = Modifier.padding(end = 16.dp),text = text, color = color, fontSize = 13.sp)
    }
}