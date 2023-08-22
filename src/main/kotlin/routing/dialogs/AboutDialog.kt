package routing.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.MyIconButton
import org.jetbrains.skiko.Cursor
import theme.ColorBox

@Composable
fun AboutDialog(
    closeClicked : () -> Unit,
) {

    val uriHandler = LocalUriHandler.current

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
                text = "About",
                color = ColorBox.text.copy(0.8f),
                fontSize = 18.sp
            )
        }
        Spacer(Modifier.padding(4.dp))
        Image(
            modifier = Modifier.size(110.dp),
            painter = painterResource("icons/app_icon.svg"),
            contentDescription = null
        )
        Spacer(Modifier.padding(6.dp))
        Text(
            text = "Plaudio",
            fontSize = 20.sp,
            color = ColorBox.text
        )
        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = "1.0.0",
            fontSize = 14.sp,
            color = ColorBox.text.copy(0.7f)
        )
        Text(
            modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp),
            text = "An Open Source Beautiful Audio player\n& Tag Editor",
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            color = ColorBox.text.copy(0.8f)
        )
        Row(
            modifier = Modifier.padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Copyright © 2023 ",
                fontSize = 11.sp,
                color = ColorBox.text.copy(0.7f)
            )
            ClickableText(
                modifier = Modifier.pointerHoverIcon(icon = PointerIcon(Cursor(Cursor.HAND_CURSOR))),
                text = AnnotatedString("SudoDios"),
                onClick = {
                    uriHandler.openUri("https://github.com/SudoDios")
                },
                style = TextStyle(
                    fontSize = 11.sp,
                    color = ColorBox.primary
                )
            )
        }
        Row(
            modifier = Modifier.padding(top = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Licenced On ",
                fontSize = 11.sp,
                color = ColorBox.text.copy(0.7f)
            )
            ClickableText(
                modifier = Modifier.pointerHoverIcon(icon = PointerIcon(Cursor(Cursor.HAND_CURSOR))),
                text = AnnotatedString("Apache License 2.0"),
                onClick = {
                    uriHandler.openUri("https://www.apache.org/licenses/LICENSE-2.0")
                },
                style = TextStyle(
                    fontSize = 11.sp,
                    color = ColorBox.primary
                )
            )
        }
        ClickableText(
            modifier = Modifier.padding(top = 6.dp, bottom = 20.dp).pointerHoverIcon(icon = PointerIcon(Cursor(Cursor.HAND_CURSOR))),
            text = AnnotatedString("View Source Code"),
            onClick = {
                uriHandler.openUri("https://github.com/SudoDios/Plaudio")
            },
            style = TextStyle(
                fontSize = 11.sp,
                color = ColorBox.primary
            )
        )
    }

}

