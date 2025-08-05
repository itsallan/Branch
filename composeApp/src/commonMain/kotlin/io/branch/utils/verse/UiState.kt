package io.branch.utils.verse

import io.branch.utils.audio.AudioState

data class UiState(
    val verseData: VerseOfTheDay = VerseOfTheDay(),
    val audioState: AudioState = AudioState(),
    val isLoading: Boolean = true,
    val error: String? = null
)