package utils

import java.util.*

object Package {

    private object OSDetector {

        private val OS = System.getProperty("os.name").lowercase(Locale.getDefault())

        private val isWindows: Boolean
            get() = OS.contains("win")
        private val isMac: Boolean
            get() = OS.contains("mac")
        private val isUnix: Boolean
            get() = OS.contains("nix") || OS.contains("nux") || OS.contains("aix")

        val detect : Os
            get() {
                return when {
                    isWindows -> Os.Window
                    isMac -> Os.Mac
                    isUnix -> Os.Unix
                    else -> Os.Unknown
                }
            }

        enum class Os {
            Window,
            Mac,
            Unix,
            Unknown
        }
    }

}