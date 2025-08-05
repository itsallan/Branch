package io.branch.utils.verse

import io.branch.utils.audio.AudioState

data class VerseUIState(
    val verse: VerseOfTheDay,
    val audioState: AudioState,
    val isLoading: Boolean = false,
    val isAudioLoading: Boolean = false,
    val isFavorited: Boolean = false,
    val error: String? = null
)