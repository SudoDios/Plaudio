package components.menu

import androidx.compose.runtime.Composable
import utils.Prefs

@Composable
fun HomeAppbarPopup(
    show : Boolean = false,
    onDismissRequest : () -> Unit,
    callback : (Int) -> Unit
) {

    CustomDropdownMenu(
        expanded = show,
        onDismissRequest = onDismissRequest
    ) {
        MenuItem(if (Prefs.isDarkMode) "icons/sun.svg" else "icons/moon.svg",if (Prefs.isDarkMode) "Light Mode" else "Dark Mode") {
            callback.invoke(0)
        }
        MenuItem("icons/refresh.svg","ReSync Audios (WIP)") {
            callback.invoke(1)
        }
        MenuItem("icons/info.svg","About") {
            callback.invoke(2)
        }
    }

}

