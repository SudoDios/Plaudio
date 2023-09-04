package routing.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.CustomOcState
import components.HamburgerMenu
import components.menu.HomeAppbarPopup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import routing.dialogs.AboutDialog
import ru.alexgladkov.odyssey.compose.extensions.present
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.compose.navigation.modal_navigation.AlertConfiguration
import theme.ColorBox
import utils.Global
import utils.clickable

@Composable
fun Appbar(
    showMenuIcon: Boolean,
    drawerState: CustomOcState,
    onSearchContent: (String) -> Unit,
    scope: CoroutineScope,
) {

    val modalController = LocalRootController.current.findModalController()
    var showAppbarMenu by remember { mutableStateOf(false) }


    Column(Modifier.height(Global.APPBAR_HEIGHT.dp).clickable().fillMaxWidth().background(ColorBox.primaryDark.copy(0.4f)), verticalArrangement = Arrangement.Center) {
        Row(Modifier.padding(start = 16.dp, top = 8.dp,end = 8.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            if (showMenuIcon) {
                HamburgerMenu(modifier = Modifier.size(48.dp), size = 20.dp, isOpen = drawerState.isOpen) {
                    if (drawerState.isOpen) {
                        scope.launch {
                            drawerState.close()
                        }
                    } else {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                }
                Spacer(Modifier.padding(6.dp))
            }
            searchTextField {
                onSearchContent.invoke(it)
            }
        }
        Spacer(Modifier.padding(5.dp))
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 12.dp),
            textAlign = TextAlign.Center,
            text = Global.Data.currentFolder.value.name,
            color = ColorBox.text,
            fontSize = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.padding(2.dp))
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 12.dp,bottom = 8.dp),
            text = "${Global.Data.currentFolder.value.childCunt} songs",
            color = ColorBox.text.copy(0.7f),
            fontSize = 14.sp
        )
    }

    HomeAppbarPopup(
        show = showAppbarMenu,
        onDismissRequest = {
            showAppbarMenu = false
        },
        callback = {
            when (it) {
                0 -> {
                    ColorBox.switchDarkLight()
                }

                1 -> {

                }

                2 -> {
                    modalController.present(AlertConfiguration(alpha = 0.6f, cornerRadius = 6)) {
                        AboutDialog {
                            modalController.popBackStack(null)
                        }
                    }
                }
            }
            showAppbarMenu = false
        }
    )

}


@Composable
private fun searchTextField(
    onValueChange: (String) -> Unit,
) {

    var value by remember { mutableStateOf(TextFieldValue()) }

    Box(
        Modifier.height(48.dp).widthIn(max = 450.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(ColorBox.card.copy(0.8f)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(start = 12.dp).size(20.dp),
                painter = painterResource("icons/search-normal.svg"),
                contentDescription = null,
                tint = ColorBox.text.copy(0.6f)
            )
            BasicTextField(
                modifier = Modifier.padding(12.dp).weight(1f)
                    .onFocusChanged { Global.Data.hasAnyTextFieldFocus = it.hasFocus },
                value = value,
                textStyle = TextStyle(
                    color = ColorBox.text.copy(0.8f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                ),
                cursorBrush = SolidColor(ColorBox.text.copy(0.9f)),
                singleLine = true,
                onValueChange = {
                    value = it
                    onValueChange.invoke(it.text)
                }
            )

        }
        if (value.text.trim().isEmpty()) {
            Text(
                modifier = Modifier.padding(start = 43.dp).fillMaxWidth(),
                text = "Search audios ...",
                fontSize = 15.sp,
                color = ColorBox.text.copy(0.3f)
            )
        }
    }

}