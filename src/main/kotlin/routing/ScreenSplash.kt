package routing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.db.CoreDB
import kotlinx.coroutines.delay
import moe.tlaster.precompose.navigation.Navigator
import routing.dialogs.BaseDialog
import routing.dialogs.SearchAudioDialog
import utils.Tools

@Composable
fun ScreenSplash(navigator: Navigator) {

    var showSyncDialog by remember { mutableStateOf(false) }

    val audioCount = CoreDB.Audios.count()

    if (audioCount > 0) {
        LaunchedEffect(Unit) {
            delay(600)
            navigator.navigate(Routes.HOME)
        }
    }

    Image(
        modifier = Modifier.fillMaxSize().blur(16.dp),
        painter = painterResource("images/splash-background.jpg"),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.clip(RoundedCornerShape(50)).background(Color.White.copy(0.3f))) {
            Icon(
                modifier = Modifier.padding(16.dp).size(110.dp),
                painter = painterResource("icons/app_icon.svg"),
                contentDescription = null,
                tint = Color.White
            )
        }
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = "Welcome to Plaudio",
            fontSize = 18.sp,
            color = Color.White
        )
        AnimatedVisibility(
            visible = audioCount == 0
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(0.1f))
                    .clickable {
                        showSyncDialog = true
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.padding(start = 12.dp).size(20.dp),
                    painter = painterResource("icons/search-normal.svg"),
                    contentDescription = null,
                    tint = Color.White.copy(0.8f)
                )
                Text(
                    modifier = Modifier.padding(start = 12.dp, end = 16.dp),
                    text = "Open Sync Settings",
                    color = Color.White.copy(0.9f),
                    fontSize = 13.sp
                )
            }
        }
    }

    BaseDialog(
        expanded = showSyncDialog
    ) {
        SearchAudioDialog(
            onFinished = {
                showSyncDialog = false
                Tools.postDelayed(500) {
                    navigator.navigate(Routes.HOME)
                }
            },
            onClose = {
                showSyncDialog = false
            }
        )
    }

}