package core

import core.db.models.ModelAudio
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent
import utils.Global
import utils.Prefs
import utils.Tools

object CorePlayer {

    private lateinit var audioPlayerComponent: AudioPlayerComponent

    private var listIndexCallback : MutableLiveData<Int?> = MutableLiveData(null)
    var visiblePlayer = MutableLiveData(false)
    var currentMediaItemCallback = MutableLiveData(ModelAudio())
    var progressCallback = MutableLiveData(0f)
    var playPauseCallback = MutableLiveData(false)
    var isMutedCallback = MutableLiveData(false)
    var isEqEnableCallback = MutableLiveData(Prefs.equalizerOn)
    var volumeChangedCallback = MutableLiveData(100)

    private var isInitiate = false
    fun init () {
        if (!isInitiate) {
            isInitiate = true
            audioPlayerComponent = AudioPlayerComponent()
            audioPlayerComponent.mediaPlayer().events().addMediaPlayerEventListener(object : MediaPlayerEventAdapter(){
                override fun playing(mediaPlayer: MediaPlayer?) {
                    playPauseCallback.value = true
                }
                override fun paused(mediaPlayer: MediaPlayer?) {
                    playPauseCallback.value = false
                }
                override fun finished(mediaPlayer: MediaPlayer?) {
                    when (Prefs.repeatMode) {
                        Prefs.REPEAT_MODE_ALL -> {
                            Tools.postDelayed(200) {
                                next()
                            }
                        }
                        Prefs.REPEAT_MODE_ONE -> {
                            Tools.postDelayed(200) {
                                startPlay(currentMediaItemCallback.value,null)
                            }
                        }
                        Prefs.REPEAT_MODE_SHUFFLE -> {
                            Tools.postDelayed(200) {
                                shuffle()
                            }
                        }
                        Prefs.REPEAT_MODE_STOP -> {
                            Tools.postDelayed(200) {
                                stop()
                            }
                        }
                    }
                }
                override fun positionChanged(mediaPlayer: MediaPlayer?, newPosition: Float) {
                    progressCallback.value = newPosition
                }
                override fun muted(mediaPlayer: MediaPlayer?, muted: Boolean) {
                    isMutedCallback.value = muted
                }
                override fun volumeChanged(mediaPlayer: MediaPlayer?, volume: Float) {
                    if (volume in 0f..1.2f) {
                        volumeChangedCallback.value = (volume * 100).toInt()
                    }
                }
                override fun error(mediaPlayer: MediaPlayer?) {
                    stop(false)
                }
            })
            if (Prefs.equalizerOn) {
                turnOnEqualizer(Prefs.equalizerPreset)
            } else {
                turnOffEqualizer()
            }
        }
    }

    val isPlaying : Boolean
        get() {
            return audioPlayerComponent.mediaPlayer().status().isPlaying
        }

    private var job : Job? = null
    fun startPlay (musicItem: ModelAudio,index: Int? = null) {
        listIndexCallback.value = index
        currentMediaItemCallback.value = musicItem
        visiblePlayer.value = true
        if (job != null && job?.isActive!!) {
            job?.cancel()
        }
        job = CoroutineScope(Dispatchers.IO).launch {
            audioPlayerComponent.mediaPlayer().media().prepare(musicItem.path)
            audioPlayerComponent.mediaPlayer().controls().play()
        }
    }

    fun play () {
        audioPlayerComponent.mediaPlayer().controls().play()
    }

    fun pause () {
        audioPlayerComponent.mediaPlayer().controls().pause()
    }

    fun autoPlayPause () {
        if (isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun stop (release : Boolean = true) {
        if (release) audioPlayerComponent.mediaPlayer().controls().stop()
        currentMediaItemCallback.value = ModelAudio()
        progressCallback.value = 0f
        playPauseCallback.value = false
        visiblePlayer.value = false
    }

    fun changeSpeed (speed : Float) {
        audioPlayerComponent.mediaPlayer().controls().setRate(speed)
    }

    fun changeVolume (volume : Int) {
        audioPlayerComponent.mediaPlayer().audio().setVolume(volume)
    }

    fun incVolume () {
        var currentVolume = audioPlayerComponent.mediaPlayer().audio().volume()
        currentVolume += 5
        if (currentVolume > 120) {
            audioPlayerComponent.mediaPlayer().audio().setVolume(120)
        } else {
            audioPlayerComponent.mediaPlayer().audio().setVolume(currentVolume)
        }
    }

    fun decVolume () {
        var currentVolume = audioPlayerComponent.mediaPlayer().audio().volume()
        currentVolume -= 5
        if (currentVolume < 0) {
            audioPlayerComponent.mediaPlayer().audio().setVolume(0)
        } else {
            audioPlayerComponent.mediaPlayer().audio().setVolume(currentVolume)
        }
    }


    fun turnOffEqualizer () {
        audioPlayerComponent.mediaPlayer().audio().setEqualizer(null)
        Prefs.equalizerOn = false
        isEqEnableCallback.value = false
    }
    fun turnOnEqualizer (name : String) {
        val equalizer = audioPlayerComponent.mediaPlayerFactory().equalizer().newEqualizer(name)
        audioPlayerComponent.mediaPlayer().audio().setEqualizer(equalizer)
        Prefs.equalizerOn = true
        Prefs.equalizerPreset = name
        isEqEnableCallback.value = true
    }
    fun getEqualizerList () : Map<String,FloatArray> {
        return audioPlayerComponent.mediaPlayerFactory().equalizer().allPresetEqualizers().mapValues { it.value.amps() }
    }


    fun seekTo (pos : Float) {
        audioPlayerComponent.mediaPlayer().controls().setPosition(pos)
        progressCallback.value = pos
    }

    fun forward (timeMillis : Long) {
        audioPlayerComponent.mediaPlayer().controls().skipTime(timeMillis)
    }

    fun backward (timeMillis : Long) {
        audioPlayerComponent.mediaPlayer().controls().skipTime(-timeMillis)
    }

    fun autoMute () {
        audioPlayerComponent.mediaPlayer().audio().mute()
    }

    fun next () {
        if (Global.Data.filteredAudioList.isNotEmpty()) {
            val currentIndex = Global.Data.filteredAudioList.indexOfFirst { it.id ==  currentMediaItemCallback.value.id }
            if (currentIndex == -1 || currentIndex == (Global.Data.filteredAudioList.size - 1)) {
                startPlay(Global.Data.filteredAudioList[0],0)
            } else {
                startPlay(Global.Data.filteredAudioList[currentIndex+1],currentIndex+1)
            }
        }
    }

    private fun shuffle () {
        if (Global.Data.filteredAudioList.isNotEmpty()) {
            val shuffleItem = Global.Data.filteredAudioList.random()
            startPlay(shuffleItem,null)
        }
    }

    fun previous () {
        if (audioPlayerComponent.mediaPlayer().status().time() > 5000) {
            seekTo(0f)
        } else {
            if (Global.Data.filteredAudioList.isNotEmpty()) {
                val currentIndex = Global.Data.filteredAudioList.indexOfFirst { it.id == currentMediaItemCallback.value.id }
                if (currentIndex == -1) {
                    startPlay(Global.Data.filteredAudioList[0],0)
                } else {
                    if (currentIndex == 0) {
                        startPlay(Global.Data.filteredAudioList.last(), Global.Data.filteredAudioList.size)
                    } else {
                        startPlay(Global.Data.filteredAudioList[currentIndex-1],currentIndex-1)
                    }
                }
            }
        }
    }

}