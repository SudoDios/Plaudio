package components.menu

import androidx.compose.runtime.Composable
import utils.Global

@Composable
fun SortListPopup(
    show : Boolean = false,
    onDismissRequest : () -> Unit,
) {

    CustomDropdownMenu(
        expanded = show,
        onDismissRequest = onDismissRequest
    ) {
        MenuItem("icons/sort-asc.svg","Name A to Z") {
            if (Global.Data.sortType.value != Global.Data.SORT_NAME_ASC) {
                Global.Data.sortType.value = Global.Data.SORT_NAME_ASC
                Global.Data.reFetchMainList()
            }
            onDismissRequest.invoke()
        }
        MenuItem("icons/sort-desc.svg","Name Z to A") {
            if (Global.Data.sortType.value != Global.Data.SORT_NAME_DESC) {
                Global.Data.sortType.value = Global.Data.SORT_NAME_DESC
                Global.Data.reFetchMainList()
            }
            onDismissRequest.invoke()
        }
        MenuItem("icons/sort-asc.svg","Duration Ascending") {
            if (Global.Data.sortType.value != Global.Data.SORT_DURATION_ASC) {
                Global.Data.sortType.value = Global.Data.SORT_DURATION_ASC
                Global.Data.reFetchMainList()
            }
            onDismissRequest.invoke()
        }
        MenuItem("icons/sort-desc.svg","Duration Descending") {
            if (Global.Data.sortType.value != Global.Data.SORT_DURATION_DESC) {
                Global.Data.sortType.value = Global.Data.SORT_DURATION_DESC
                Global.Data.reFetchMainList()
            }
            onDismissRequest.invoke()
        }
    }

}