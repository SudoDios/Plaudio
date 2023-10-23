package core.db.models

import androidx.compose.ui.graphics.Color

data class ModelDropFiles(
    var extension : String,
    var name : String,
    var state : Int,
    var color : Color
) {

    companion object {
        const val STATE_ADDED = 0
        const val STATE_AVAILABLE = 1
        const val STATE_NOT_SUPPORT = 2
    }

}
