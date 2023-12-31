package utils

import dev.icerock.moko.mvvm.livedata.MutableLiveData
import java.util.prefs.Preferences

object Prefs {

    const val REPEAT_MODE_ALL = 0
    const val REPEAT_MODE_ONE = 1
    const val REPEAT_MODE_SHUFFLE = 2
    const val REPEAT_MODE_STOP = 3

    private val preferences = Preferences.userRoot()

    var playbackSpeed : Float = 0.33f

    var repeatModeChanged = MutableLiveData(repeatMode)
    var repeatMode : Int
        get() {
            return preferences.getInt("repeatMode",REPEAT_MODE_ALL)
        }
        set(value) {
            repeatModeChanged.value = value
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

    var isDarkMode : Boolean
        get() {
            return preferences.getBoolean("isDarkMode",true)
        }
        set(value) {
            preferences.putBoolean("isDarkMode",value)
        }

    //initiate
    var isFirstInitialized : Boolean
        get() {
            return preferences.getBoolean("isFirstInitialized",false)
        }
        set(value) {
            preferences.putBoolean("isFirstInitialized",value)
        }

}