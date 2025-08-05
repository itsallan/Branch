package io.branch.data.audio

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.currentItem
import platform.AVFoundation.currentTime
import platform.AVFoundation.duration
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.CoreMedia.CMTimeGetSeconds
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSURL
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
actual class PlatformAudioPlayer actual constructor() : AudioPlayer {
    private var player: AVPlayer? = null
    private var isPlayingState: Boolean = false
    private var observer: NSObject? = null

    actual override fun play(url: String) {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl != null) {
            if (player == null) {
                val playerItem = AVPlayerItem.playerItemWithURL(nsUrl)
                player = AVPlayer.playerWithPlayerItem(playerItem)
            }
            player?.play()
            isPlayingState = true
        }
    }

    actual override fun pause() {
        player?.pause()
        isPlayingState = false
    }

    actual override fun release() {
        observer?.let {
            NSNotificationCenter.defaultCenter.removeObserver(it)
        }
        observer = null
        player = null
        isPlayingState = false
    }

    actual override fun isPlaying(): Boolean {
        return isPlayingState
    }

    actual override fun getCurrentPosition(): Long {
        val time = player?.currentTime() ?: return 0L
        // Using CMTimeGetSeconds for correct time conversion
        return (CMTimeGetSeconds(time) * 1000).toLong()
    }

    actual override fun getDuration(): Long {
        val duration = player?.currentItem?.duration ?: return 0L
        // Using CMTimeGetSeconds for correct time conversion
        return (CMTimeGetSeconds(duration) * 1000).toLong()
    }
}