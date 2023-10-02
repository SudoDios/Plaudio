package components.menu

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AudioListPopup(
    show : Boolean,
    isFav : Boolean,
    onDismissRequest : () -> Unit,
    callback : (Int) -> Unit
) {

    CustomDropdownMenu(
        expanded = show,
        onDismissRequest = onDismissRequest
    ) {
        if (isFav) {
            MenuItem("icons/heart.svg","Remove from favorites") {
                callback.invoke(0)
            }
        } else {
            MenuItem("icons/heart-bold.svg","Add to favorites", iconTint = Color.Red) {
                callback.invoke(1)
            }
        }
        MenuItem("icons/magic.svg","Edit") {
            callback.invoke(2)
        }
        MenuItem("icons/info.svg","Properties") {
            callback.invoke(3)
        }
    }

}