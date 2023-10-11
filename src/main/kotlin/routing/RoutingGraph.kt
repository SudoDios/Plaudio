package routing

import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder

object Routes {
    const val SPLASH = "splash"
    const val HOME = "home"
}

fun RootComposeBuilder.routingGraph() {
    screen(Routes.SPLASH) {
        ScreenSplash()
    }
    screen(Routes.HOME) {
        ScreenHome()
    }
}