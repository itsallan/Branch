package io.branch.data.audio

import android.content.Context
import android.media.MediaPlayer

actual class PlatformAudioPlayer actual constructor() : AudioPlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var context: Context? = null

    fun setContext(context: Context) {
        this.context = context
    }

    actual override fun play(url: String) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(url)
                prepare()
                start()
            }
        } else if (!mediaPlayer!!.isPlaying) {
            mediaPlayer?.start()
        }
    }

    actual override fun pause() {
        mediaPlayer?.pause()
    }

    actual override fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    actual override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    actual override fun getCurrentPosition(): Long {
        return mediaPlayer?.currentPosition?.toLong() ?: 0L
    }

    actual override fun getDuration(): Long {
        return mediaPlayer?.duration?.toLong() ?: 0L
    }
}