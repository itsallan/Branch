package io.branch.view.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import io.branch.data.audio.PlatformAudioPlayer
import io.branch.view.components.content.ScriptureContent
import io.branch.view.components.header.AppHeader
import io.branch.view.components.shared.HomeIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {
    val coroutineScope = remember { CoroutineScope(Dispatchers.Main) }
    val audioPlayer = remember { PlatformAudioPlayer() }
    val viewModel = remember { VerseViewModel(audioPlayer, coroutineScope) }
    var isVerseFavorited by remember { mutableStateOf(false) }

    val verseUiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AppHeader(
            )

            ScriptureContent(
                verse = verseUiState.verseData,
                audioState = verseUiState.audioState,
                onPlayAudio = { viewModel.playAudio() },
                onVerseCardClick = {

                },
                onVerseFavoriteClick = {
                    isVerseFavorited = !isVerseFavorited
                },
                onVerseShareClick = {
                    // Handle verse share
                },
                onVerseBibleClick = {
                    // Handle open in bible
                },
                isVerseLoading = verseUiState.isLoading,
                isAudioLoading = false,
                verseError = verseUiState.error,
                isVerseFavorited = isVerseFavorited,
                navController = navController,
                modifier = Modifier.weight(1f)
            )

            HomeIndicator()
        }
    }
}