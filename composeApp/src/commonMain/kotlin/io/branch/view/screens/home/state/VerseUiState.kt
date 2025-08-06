package io.branch.view.screens.home.state

import io.branch.view.screens.home.model.VerseOfTheDay

data class VerseUiState(
    val verseData: VerseOfTheDay = VerseOfTheDay(),
    val audioState: AudioState = AudioState(),
    val isLoading: Boolean = true,
    val error: String? = null
)