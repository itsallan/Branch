package io.branch.utils.audio

data class AudioState(
    val isPlaying: Boolean = false,
    val progress: Float = 0f,
    val currentPosition: String = "0:00",
    val duration: String = "0:00"
)