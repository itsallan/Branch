package io.branch.view.components.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.branch.utils.verse.VerseActions
import io.branch.view.components.cards.utils.TopFadeOverlay
import io.branch.utils.verse.VerseUIState

@Composable
fun ScriptureContent(
    uiState: VerseUIState,
    actions: VerseActions,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(topStart = 44.dp, topEnd = 44.dp)
                )
                .padding(top = 34.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 32.dp,
                top = 24.dp,
                bottom = 24.dp
            )
        ) {
            item {
                VerseSection(
                    uiState = uiState,
                    actions = actions,
                    navController = navController
                )
            }
        }

        TopFadeOverlay()
    }
}