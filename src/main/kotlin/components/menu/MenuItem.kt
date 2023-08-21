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

@Composable
fun MenuItem (icon : String,text : String,onClick : () -> Unit) {
    Row(modifier = Modifier.height(40.dp).fillMaxWidth().widthIn(min = 160.dp).clickable { onClick.invoke() }, verticalAlignment = Alignment.CenterVertically) {
        Spacer(Modifier.padding(6.dp))
        Icon(
            modifier = Modifier.size(22.dp),
            painter = painterResource(icon),
            contentDescription = null,
            tint = ColorBox.text.copy(0.7f)
        )
        Spacer(Modifier.padding(6.dp))
        Text(modifier = Modifier.padding(end = 16.dp),text = text, color = ColorBox.text.copy(0.7f), fontSize = 13.sp)
    }
}