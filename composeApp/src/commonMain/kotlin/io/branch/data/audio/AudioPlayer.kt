package io.branch.data.audio

interface AudioPlayer {
    fun play(url: String)
    fun pause()
    fun release()
    fun isPlaying(): Boolean
    fun getCurrentPosition(): Long
    fun getDuration(): Long
}

expect class PlatformAudioPlayer() : AudioPlayer {
    override fun play(url: String)
    override fun pause()
    override fun release()
    override fun isPlaying(): Boolean
    override fun getCurrentPosition(): Long
    override fun getDuration(): Long
}