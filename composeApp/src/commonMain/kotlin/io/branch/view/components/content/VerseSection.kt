package io.branch.view.components.content

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import io.branch.utils.verse.VerseActions
import io.branch.utils.verse.VerseUIState
import io.branch.view.components.cards.utils.ErrorCard
import io.branch.view.components.cards.utils.LoadingCard

@Composable
fun VerseSection(
    uiState: VerseUIState,
    actions: VerseActions,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        when {
            uiState.isLoading -> LoadingCard()
            uiState.error != null -> ErrorCard(uiState.error)
            else -> VerseContent(uiState, actions, navController)
        }
    }
}