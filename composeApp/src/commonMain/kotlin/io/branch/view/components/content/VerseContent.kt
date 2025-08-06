package io.branch.view.components.content

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.branch.utils.verse.VerseActions
import io.branch.utils.verse.VerseUIState
import io.branch.view.components.cards.PrayerCard
import io.branch.view.components.cards.ReflectionCard
import io.branch.view.components.cards.VerseCard

@Composable
fun VerseContent (
    uiState: VerseUIState,
    actions: VerseActions,
    navController: NavController
    ) {
    VerseCard(
        devotional = uiState.verse,
        navController = navController
    )

    ReflectionCard(
        verse = uiState.verse,
        audioState = uiState.audioState,
        onPlayAudio = actions.onPlayAudio,
        onFavoriteClick = actions.onFavoriteClick,
        onShareClick = actions.onShareClick,
        onBibleClick = actions.onBibleClick,
        onCardClick = actions.onCardClick,
        isAudioLoading = uiState.isAudioLoading,
        isFavorited = uiState.isFavorited
    )

    PrayerCard(verse = uiState.verse)
}