package br.dev.nina.sprachklang.word.presentation.audioplayer

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AudioPlayerManager @Inject constructor() {
    private var mediaPlayer: MediaPlayer? = null

    private val _currentPlayingMedia = MutableStateFlow<Uri?>(null)
    val currentPlayingMedia: StateFlow<Uri?> = _currentPlayingMedia

    fun playMedia(context: Context, audioUri: Uri) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().also { player ->
                player.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                player.setOnCompletionListener {
                    _currentPlayingMedia.value = null
                }
            }
        }

        if (_currentPlayingMedia.value == audioUri) {
            // Restart audio in case is currently playing
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
            }
            mediaPlayer?.start()
            return
        }

        mediaPlayer?.apply {
            // Clear the player every time before a new datasource
            stop()
            reset()

            setDataSource(context, audioUri)
            prepareAsync()
            setOnPreparedListener {
                it.start()
                _currentPlayingMedia.value = audioUri
            }
        }
    }

    fun stopMedia() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
                reset()
            }
            _currentPlayingMedia.value = null
        }
    }

    fun clear() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
