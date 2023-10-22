import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.sun.jna.NativeLibrary
import core.CorePlayer
import core.db.CoreDB
import routing.routingGraph
import ru.alexgladkov.odyssey.compose.setup.OdysseyConfiguration
import ru.alexgladkov.odyssey.compose.setup.setNavigationContent
import theme.ColorBox
import theme.Fonts
import uk.co.caprica.vlcj.binding.support.runtime.RuntimeUtil
import utils.Global
import utils.Prefs
import java.awt.Dimension
import java.io.File
import java.util.logging.LogManager

@Composable
fun App() {
    //init
    System.setProperty("java.util.prefs.userRoot",Global.prefsPath)
    NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),Global.libVLCDir)
    if (!Prefs.isFirstInitialized) {
        File(Global.coverPaths).deleteRecursively()
        File(Global.dbPath).deleteRecursively()
    }
    File(Global.coverPaths).mkdirs()
    File(Global.dbPath).mkdirs()
    CoreDB.init()

    MaterialTheme(
        typography = Fonts.typography,
        colors = MaterialTheme.colors.copy(
            isLight = !ColorBox.isDarkMode,
            surface = ColorBox.card,
            primary = ColorBox.primary
        )
    ) {
        setNavigationContent(
            OdysseyConfiguration(backgroundColor = if (Prefs.isDarkMode) Color.Black else Color.White),
            onApplicationFinish = {}) {
            routingGraph()
        }
    }

    LogManager.getLogManager().reset()
}


fun main() = application {
    Global.requestCloseWindow.addObserver {
        exitApplication()
    }
    Window(
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
                        return@Window true
                    }

                    (it.key == Key.DirectionRight && it.type == KeyEventType.KeyUp) -> {
                        CorePlayer.forward(15000)
                        return@Window true
                    }

                    (it.key == Key.DirectionLeft && it.type == KeyEventType.KeyUp) -> {
                        CorePlayer.backward(10000)
                        return@Window true
                    }

                    (it.key == Key.DirectionUp && it.type == KeyEventType.KeyUp) -> {
                        CorePlayer.incVolume()
                        return@Window true
                    }

                    (it.key == Key.DirectionDown && it.type == KeyEventType.KeyUp) -> {
                        CorePlayer.decVolume()
                        return@Window true
                    }
                }
            } else {
                return@Window false
            }
            false
        }
    ) {
        window.minimumSize = Dimension(510, 670)
        App()
    }
}
