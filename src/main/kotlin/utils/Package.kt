package utils

import com.sun.jna.Native
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.co.caprica.vlcj.binding.lib.LibVlc
import uk.co.caprica.vlcj.support.version.LibVlcVersion
import java.io.File
import java.util.*

object Package {

    var vlcVersion : String = ""

    fun isLibvlcInstalled () : Boolean {
        Native.unregister(LibVlc::class.java)
        return try {
            vlcVersion = LibVlcVersion().version.version()
            true
        } catch (e : UnsatisfiedLinkError) {
            false
        }
    }

    fun clearVlcFiles () {
        File(Global.libVLCDir).apply {
            deleteRecursively()
            mkdirs()
        }
    }

    fun checkVlcFilesExist () : Boolean {
        val vlcDir = File(Global.libVLCDir)
        vlcDir.mkdirs()
        return vlcDir.listFiles()!!.isNotEmpty()
    }

    private const val VLC_LINUX_X64_S_NAME = "vlc-linux-x64.zip"
    private const val VLC_WIN_X64_S_NAME = "vlc-win-x64.zip"

    fun initiateVlcFiles (progress : (Float) -> Unit,state : (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            File(Global.libVLCDir).mkdirs()
            val fileZipName = if (Os.isUnix) VLC_LINUX_X64_S_NAME else if (Os.isWindows) VLC_WIN_X64_S_NAME else ""
            //copy zip to app folder
            if (fileZipName.isNotEmpty()) {
                val stream = javaClass.classLoader.getResourceAsStream(fileZipName)
                FileUtils.copyFile(stream,"${Global.libVLCDir}/$fileZipName") {
                    state.invoke("Coping files ...")
                    progress.invoke(it / 2f)
                }
                //extract zip and remove it
                FileUtils.UnzipUtils.unzip(File("${Global.libVLCDir}/$fileZipName"),Global.libVLCDir) {
                    state.invoke("Extract files ...")
                    progress.invoke((it / 2f) + .5f)
                }
                //remove zip file
                File("${Global.libVLCDir}/$fileZipName").delete()
                withContext(Dispatchers.Main) {
                    state.invoke("Completed")
                }
            } else {
                state.invoke("Not support")
            }
        }
    }

    object Os {

        private val OS = System.getProperty("os.name").lowercase(Locale.getDefault())

        val isWindows: Boolean
            get() = OS.contains("win")
        val isMac: Boolean
            get() = OS.contains("mac")
        val isUnix: Boolean
            get() = OS.contains("nix") || OS.contains("nux") || OS.contains("freebsd")

    }

}