package routing.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.CorePlayer
import kotlinx.coroutines.delay
import theme.ColorBox
import utils.Package

@Composable
fun SetupDialog(success : () -> Unit) {

    var state by remember { mutableStateOf("Preparing ...") }
    var progress by remember { mutableStateOf(-1f) }

    LaunchedEffect(Unit) {
        delay(2000)
        Package.initiateVlcFiles(
            progress = {
                progress = it
            },
            state = {
                state = it
                when (state) {
                    "Completed" -> {
                        if (Package.isLibvlcInstalled()) {
                            CorePlayer.init()
                            success.invoke()
                        }
                    }
                    "Not support" -> {

                    }
                }
            }
        )
    }

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
                text = "Setup Libraries",
                color = ColorBox.text.copy(0.8f),
                fontSize = 18.sp
            )
        }
        Icon(
            modifier = Modifier.padding(20.dp).size(85.dp),
            painter = painterResource("icons/cpu-setting.svg"),
            contentDescription = null,
            tint = ColorBox.text
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Please wait for config core libraries",
            color = ColorBox.text.copy(0.9f),
            fontSize = 14.sp
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = state,
            color = ColorBox.text.copy(0.7f),
            fontSize = 13.sp
        )
        if (progress != -1f) {
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.padding(top = 20.dp, bottom = 25.dp).width(120.dp),
                strokeCap = StrokeCap.Round
            )
        } else {
            LinearProgressIndicator(
                modifier = Modifier.padding(top = 20.dp, bottom = 25.dp).width(120.dp),
                strokeCap = StrokeCap.Round
            )
        }
    }

}