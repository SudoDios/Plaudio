import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
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
import core.CorePlayer
import core.db.CoreDB
import core.db.models.ModelAudio
import core.db.models.ModelFolder
import routing.routingGraph
import ru.alexgladkov.odyssey.compose.setup.OdysseyConfiguration
import ru.alexgladkov.odyssey.compose.setup.setNavigationContent
import theme.ColorBox
import theme.Fonts
import utils.Prefs
import utils.Tools
import java.awt.Dimension
import java.io.File
import java.util.logging.LogManager

object CenterState {

    var hasAnyTextFieldFocus = false

    val audioList = ArrayList<ModelAudio>().apply {
        addAll(CoreDB.Audios.read())
    }
    val filteredAudioList = SnapshotStateList<ModelAudio>().apply {
        addAll(audioList)
    }
    val folderList = SnapshotStateList<ModelFolder>().apply {
        addAll(CoreDB.Folders.read())
    }

    /*states*/
    var currentFolder = mutableStateOf(ModelFolder(childCunt = audioList.size))

}

@Composable
fun App() {
    //init
    File(Tools.coverPaths).mkdirs()
    File(Tools.dbPath).mkdirs()
    CoreDB.init()
    CorePlayer.init()


    MaterialTheme(
        typography = Fonts.typography,
        colors = MaterialTheme.colors.copy(
            isLight = !ColorBox.isDarkMode,
            surface = ColorBox.card,
            primary = ColorBox.primary
        )
    ) {
        setNavigationContent(OdysseyConfiguration(backgroundColor = if (Prefs.isDarkMode) Color.Black else Color.White), onApplicationFinish = {}) {
            routingGraph()
        }
    }

    LogManager.getLogManager().reset()
}


@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    Window(
        icon = painterResource("icons/app_icon.png"),
        onCloseRequest = ::exitApplication,
        title = "Plaudio",
        state = WindowState(size = DpSize(400.dp,645.dp)),
        resizable = false,
        onKeyEvent = {
            if (!CenterState.hasAnyTextFieldFocus) {
                when  {
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
        window.minimumSize = Dimension(400,645)
        App()
    }
}
