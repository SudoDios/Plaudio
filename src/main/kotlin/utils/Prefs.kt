package utils

import java.util.prefs.Preferences

object Prefs {

    const val REPEAT_MODE_ALL = 0
    const val REPEAT_MODE_ONE = 1
    const val REPEAT_MODE_SHUFFLE = 2
    const val REPEAT_MODE_STOP = 3

    private val preferences = Preferences.userRoot()

    var playbackSpeed : Float = 0.33f

    var repeatMode : Int
        get() {
            return preferences.getInt("repeatMode",REPEAT_MODE_ALL)
        }
        set(value) {
            preferences.putInt("repeatMode",value)
        }

    var equalizerOn : Boolean
        get() {
            return preferences.getBoolean("equalizerOn",false)
        }
        set(value) {
            preferences.putBoolean("equalizerOn",value)
        }

    var equalizerPreset : String
        get() {
            return preferences.get("equalizerPreset","Club")
        }
        set(value) {
            preferences.put("equalizerPreset",value)
        }

}