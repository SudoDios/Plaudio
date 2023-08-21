package components.menu

import androidx.compose.runtime.Composable

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
        MenuItem("icons/refresh.svg","ReSync Audios (WIP)") {
            callback.invoke(0)
        }
        MenuItem("icons/info.svg","About") {
            callback.invoke(1)
        }
    }

}

