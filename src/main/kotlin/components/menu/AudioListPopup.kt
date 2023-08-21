package components.menu

import androidx.compose.runtime.Composable
import core.db.models.ModelAudio

@Composable
fun AudioListPopup(
    show : Pair<Boolean,ModelAudio>,
    onDismissRequest : () -> Unit,
    callback : (Int,ModelAudio) -> Unit
) {

    CustomDropdownMenu(
        expanded = show.first,
        onDismissRequest = onDismissRequest
    ) {
        if (show.second.isFav) {
            MenuItem("icons/heart.svg","Remove from favorites") {
                callback.invoke(0,show.second)
            }
        } else {
            MenuItem("icons/heart-bold.svg","Add to favorites") {
                callback.invoke(1,show.second)
            }
        }
        MenuItem("icons/magic.svg","Edit") {
            callback.invoke(2,show.second)
        }
        MenuItem("icons/info.svg","Properties") {
            callback.invoke(3,show.second)
        }
    }

}