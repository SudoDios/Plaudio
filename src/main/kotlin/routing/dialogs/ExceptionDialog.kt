package routing.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.ColorBox
import utils.Global

@Composable
fun ExceptionDialog(
    title: String
) {

    Column(
        modifier = Modifier.width(370.dp).background(color = ColorBox.primaryDark2),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(56.dp).background(color = ColorBox.primaryDark.copy(0.5f)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.padding(3.dp))
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Setup Error",
                color = ColorBox.text.copy(0.8f),
                fontSize = 18.sp
            )
        }
        Icon(
            modifier = Modifier.padding(20.dp).size(85.dp),
            painter = painterResource("icons/warning.svg"),
            contentDescription = null,
            tint = ColorBox.text
        )
        Text(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp),
            text = title,
            color = ColorBox.text.copy(0.9f),
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
        Row(
            Modifier.padding(20.dp).height(44.dp).fillMaxWidth().clip(RoundedCornerShape(50))
                .background(ColorBox.primary.copy(0.1f)).clickable {
                Global.requestCloseWindow.value = true
            }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Close App"
            )
        }
    }

}