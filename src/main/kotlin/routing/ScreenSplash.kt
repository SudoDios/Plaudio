package routing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.db.CoreDB
import kotlinx.coroutines.delay
import routing.dialogs.SearchAudioDialog
import ru.alexgladkov.odyssey.compose.extensions.present
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.compose.navigation.modal_navigation.AlertConfiguration
import theme.ColorBox
import utils.Tools

@Composable
fun ScreenSplash() {

    val rootController = LocalRootController.current
    val modalController = rootController.findModalController()

    val audioCount = CoreDB.Audios.count()

    if (audioCount > 0){
        LaunchedEffect(Unit) {
            delay(600)
            rootController.present(Routes.HOME)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().animateContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(110.dp),
            painter = painterResource("icons/app_icon.png"),
            contentDescription = null
        )
        if (audioCount == 0) {
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = "Click search audio file to start",
                fontSize = 12.sp,
                color = ColorBox.text.copy(0.7f)
            )
        }
        AnimatedVisibility(
            visible = audioCount == 0
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(50))
                    .background(ColorBox.primary.copy(0.1f))
                    .clickable {
                        modalController.present(
                            AlertConfiguration(
                                cornerRadius = 12,
                                alpha = 0.6f,
                                closeOnBackdropClick = false
                            )
                        ) {
                            SearchAudioDialog { _, _ ->
                                modalController.popBackStack(null)
                                Tools.postDelayed(500) {
                                    rootController.present(Routes.HOME)
                                }
                            }
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.padding(start = 12.dp).size(20.dp),
                    painter = painterResource("icons/search-normal.svg"),
                    contentDescription = null,
                    tint = ColorBox.primary.copy(0.8f)
                )
                Text(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                    text = "Search audio files",
                    color = ColorBox.primary.copy(0.9f),
                    fontSize = 13.sp
                )
            }
        }
    }

}