package routing.sections

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.MyIconButton
import components.menu.HomeAppbarPopup
import core.db.models.ModelFolder
import routing.dialogs.AboutDialog
import ru.alexgladkov.odyssey.compose.extensions.present
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.compose.navigation.modal_navigation.AlertConfiguration
import theme.ColorBox

@Composable
fun RowScope.Appbar(currentFolder : ModelFolder,onSearchClicked : () -> Unit) {

    val modalController = LocalRootController.current.findModalController()
    var showAppbarMenu by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(start = 12.dp, end = 12.dp).weight(1f), verticalArrangement = Arrangement.Center) {
        Text(
            textAlign = TextAlign.Center,
            text = currentFolder.name,
            color = ColorBox.text,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.padding(2.dp))
        Text(
            text = "${currentFolder.childCunt} songs",
            color = ColorBox.text.copy(0.6f),
            fontSize = 12.sp
        )
    }
    MyIconButton(
        contentPadding = 10.dp,
        icon = "icons/search-normal.svg",
        colorFilter = ColorBox.text.copy(0.6f)
    ) {
        onSearchClicked.invoke()
    }
    Spacer(Modifier.padding(2.dp))
    MyIconButton(
        contentPadding = 10.dp,
        icon = "icons/more.svg",
        colorFilter = ColorBox.text.copy(0.6f)
    ) {
        showAppbarMenu = true
    }
    HomeAppbarPopup(
        show = showAppbarMenu,
        onDismissRequest = {
            showAppbarMenu = false
        },
        callback = {
            when (it) {
                0 -> {

                }
                1 -> {
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