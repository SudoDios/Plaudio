import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import core.CorePlayer
import core.db.CoreDB
import core.youtube.Proxy
import moe.tlaster.precompose.PreComposeWindow
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import routing.Routes
import routing.ScreenHome
import routing.ScreenSplash
import theme.ColorBox
import theme.Fonts
import utils.Global
import utils.Prefs
import java.awt.Dimension
import java.io.File
import java.util.logging.LogManager

@Composable
fun App() {
    //init
    System.setProperty("java.util.prefs.userRoot",Global.prefsPath)
    if (!Prefs.isFirstInitialized) {
        File(Global.coverPaths).deleteRecursively()
        File(Global.dbPath).deleteRecursively()
    }
    File(Global.coverPaths).mkdirs()
    File(Global.dbPath).mkdirs()
    CoreDB.init()
    CorePlayer.init()
    Proxy.init()

    val navigator = rememberNavigator()

    MaterialTheme(
        typography = Fonts.typography,
        colors = MaterialTheme.colors.copy(
            isLight = !ColorBox.isDarkMode,
            surface = ColorBox.card,
            primary = ColorBox.primary
        )
    ) {
        NavHost(
            navigator = navigator,
            navTransition = NavTransition(),
            initialRoute = Routes.SPLASH,
        ) {
            scene(
                route = Routes.SPLASH,
                navTransition = NavTransition(),
            ) {
                ScreenSplash(navigator)
            }
            scene(
                route = Routes.HOME,
                navTransition = NavTransition(),
            ) {
                ScreenHome()
            }
        }
    }

    LogManager.getLogManager().reset()
}


fun main() = application {
    PreComposeWindow(
        icon = painterResource("icons/app_icon.png"),
        onCloseRequest = ::exitApplication,
        title = "Plaudio",
        state = WindowState(size = DpSize(600.dp, 700.dp)),
        resizable = true,
        onKeyEvent = {
            if (!Global.Data.hasAnyTextFieldFocus) {
                when {
                    (it.key == Key.Spacebar && it.type == KeyEventType.KeyUp) -> {
                        if (CorePlayer.isPlaying) {
                            CorePlayer.pause()
                        } else {
                            CorePlayer.play()
                        }
                        return@PreComposeWindow true
                    }

                    (it.key == Key.DirectionRight && it.type == KeyEventType.KeyUp) -> {
                        CorePlayer.forward(15000)
                        return@PreComposeWindow true
                    }

                    (it.key == Key.DirectionLeft && it.type == KeyEventType.KeyUp) -> {
                        CorePlayer.backward(10000)
                        return@PreComposeWindow true
                    }

                    (it.key == Key.DirectionUp && it.type == KeyEventType.KeyUp) -> {
                        CorePlayer.incVolume()
                        return@PreComposeWindow true
                    }

                    (it.key == Key.DirectionDown && it.type == KeyEventType.KeyUp) -> {
                        CorePlayer.decVolume()
                        return@PreComposeWindow true
                    }
                }
            } else {
                return@PreComposeWindow false
            }
            false
        }
    ) {
        window.minimumSize = Dimension(510, 680)
        App()
    }
}
