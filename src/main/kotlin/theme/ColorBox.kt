package theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import utils.Prefs

object ColorBox {

    private val NIGHT_PRIMARY = Color(0xFFFFFFFF)
    private val NIGHT_PRIMARY_DARK = Color(0xFF0c1620)
    private val NIGHT_PRIMARY_DARK2 = Color(0xFF15212a)
    private val NIGHT_PRIMARY_CARD = Color(0xFF152632)
    private val NIGHT_TEXT_COLOR = Color(0xFFFFFFFF)

    private val DAY_PRIMARY = Color(0xFF0c1620)
    private val DAY_PRIMARY_DARK = Color(0xFFFFFFFF)
    private val DAY_PRIMARY_DARK2 = Color(0xFFFAFAFA)
    private val DAY_PRIMARY_DARK3 = Color(0xFFF4F4F4)
    private val DAY_TEXT_COLOR = Color(0xFF121212)

    var isDarkMode by mutableStateOf(Prefs.isDarkMode)

    var primary by mutableStateOf(if (Prefs.isDarkMode) NIGHT_PRIMARY else DAY_PRIMARY)
        private set
    var primaryDark by mutableStateOf(if (Prefs.isDarkMode) NIGHT_PRIMARY_DARK else DAY_PRIMARY_DARK)
        private set
    var primaryDark2 by mutableStateOf(if (Prefs.isDarkMode) NIGHT_PRIMARY_DARK2 else DAY_PRIMARY_DARK2)
        private set
    var card by mutableStateOf(if (Prefs.isDarkMode) NIGHT_PRIMARY_CARD else DAY_PRIMARY_DARK3)
        private set
    var text by mutableStateOf(if (Prefs.isDarkMode) NIGHT_TEXT_COLOR else DAY_TEXT_COLOR)
        private set

    fun switchDarkLight () {
        if (isDarkMode) {
            //light
            primary = DAY_PRIMARY
            primaryDark = DAY_PRIMARY_DARK
            primaryDark2 = DAY_PRIMARY_DARK2
            card = DAY_PRIMARY_DARK3
            text = DAY_TEXT_COLOR
        } else {
            //dark
            primary = NIGHT_PRIMARY
            primaryDark = NIGHT_PRIMARY_DARK
            primaryDark2 = NIGHT_PRIMARY_DARK2
            card = NIGHT_PRIMARY_CARD
            text = NIGHT_TEXT_COLOR
        }
        Prefs.isDarkMode = !isDarkMode
        isDarkMode = !isDarkMode
    }

}