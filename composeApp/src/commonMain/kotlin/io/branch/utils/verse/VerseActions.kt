package io.branch.utils.verse

data class VerseActions(
    val onPlayAudio: () -> Unit = {},
    val onFavoriteClick: () -> Unit = {},
    val onShareClick: () -> Unit = {},
    val onBibleClick: () -> Unit = {},
    val onCardClick: () -> Unit = {}
)