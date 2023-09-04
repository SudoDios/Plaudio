package routing.sections

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.MyIconButton
import core.db.models.ModelFolder
import routing.dialogs.AboutDialog
import ru.alexgladkov.odyssey.compose.extensions.present
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.compose.navigation.modal_navigation.AlertConfiguration
import theme.ColorBox
import theme.Fonts
import ui.components.DayNightAnimationIcon
import utils.Global
import utils.clickable

@Composable
fun Drawer(
    callback: () -> Unit
) {

    val modalController = LocalRootController.current.findModalController()

    Box(Modifier.width(300.dp).fillMaxHeight()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(ColorBox.primaryDark2)
        ) {
            Row(
                Modifier.fillMaxWidth().padding(start = 24.dp, top = 24.dp, bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.size(40.dp).clip(RoundedCornerShape(50)).background(ColorBox.text.copy(0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource("icons/app_icon.svg"),
                        contentDescription = null,
                        tint = ColorBox.text
                    )
                }
                Spacer(Modifier.padding(6.dp))
                Text(
                    text = "Plaudio",
                    style = TextStyle.Default.copy(
                        fontFamily = Fonts.varela,
                        brush = Brush.linearGradient(listOf(ColorBox.text, ColorBox.text.copy(0.5f))),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        letterSpacing = 3.sp
                    )
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp,bottom = 80.dp)
            ) {
                items(Global.Data.folderList) {
                    FolderItem(it, Global.Data.currentFolder.value.path == it.path) {
                        callback.invoke()
                        Global.Data.currentFolder.value = it
                        Global.Data.searchOrFilter()
                    }
                    if (it.path == "#Fav") {
                        Divider(
                            Modifier.padding(top = 6.dp, bottom = 6.dp).fillMaxWidth(),
                            color = ColorBox.text.copy(0.2f)
                        )
                    }
                }
            }
        }
        Row(Modifier.fillMaxWidth().align(Alignment.BottomCenter).background(ColorBox.card).clickable()) {
            Row(
                Modifier
                    .padding(12.dp)
                    .height(44.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(ColorBox.text.copy(0.1f))
                    .clickable {
                        ColorBox.switchDarkLight()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                DayNightAnimationIcon(
                    Modifier.padding(start = 8.dp).size(28.dp),
                    color = ColorBox.text,
                    isDay = ColorBox.isDarkMode
                )
                Text(
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                    text = "Switch theme",
                    fontSize = 14.sp,
                    color = ColorBox.text
                )
            }
            MyIconButton(
                size = 44.dp,
                contentPadding = 8.dp,
                padding = PaddingValues(top = 12.dp, bottom = 12.dp, end = 12.dp),
                icon = "icons/info.svg"
            ) {
                modalController.present(AlertConfiguration(alpha = 0.6f, cornerRadius = 7)) {
                    AboutDialog {
                        modalController.popBackStack(null)
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun FolderItem(modelFolder: ModelFolder, isSelected: Boolean, onClick: () -> Unit) {

    val icon = when (modelFolder.path) {
        "#All" -> "icons/audio-folder.svg"
        "#Fav" -> "icons/heart.svg"
        else -> "icons/folder.svg"
    }

    val color = when (modelFolder.path) {
        "#All" -> ColorBox.text
        "#Fav" -> Color.Red
        else -> ColorBox.text.copy(0.6f)
    }

    val animate = animateFloatAsState(if (isSelected) 1f else 0f, animationSpec = tween(400))
    val contentColor = lerp(ColorBox.text, ColorBox.primaryDark2, animate.value)

    Row(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(50))
            .drawBehind {
                drawCircle(
                    color = ColorBox.text,
                    radius = size.width * animate.value
                )
            }
            .onPointerEvent(PointerEventType.Enter) {

            }
            .onPointerEvent(PointerEventType.Exit) {

            }
            .clickable {
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.padding(7.dp))
        Icon(
            modifier = Modifier.size(23.dp),
            painter = painterResource(icon),
            contentDescription = null,
            tint = if (modelFolder.path == "#Fav") color else contentColor
        )
        Spacer(Modifier.padding(7.dp))
        Text(text = "${modelFolder.name} (${modelFolder.childCunt})", fontSize = 14.sp, color = contentColor)
    }

}