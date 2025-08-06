package io.branch.view.screens.home

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Lucide
import io.branch.data.audio.PlatformAudioPlayer
import io.branch.view.screens.home.components.DevotionalsContent
import io.branch.view.screens.home.components.LoadingState
import io.branch.view.screens.home.state.ErrorState
import io.branch.view.screens.verses.VerseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
){
    val coroutineScope = remember { CoroutineScope(Dispatchers.Main) }
    val audioPlayer = remember { PlatformAudioPlayer() }
    val viewModel = remember { VerseViewModel(audioPlayer, coroutineScope) }
    val devotionalsViewModel = remember { DevotionalsViewModel() }
    val uiState by viewModel.uiState.collectAsState()
    val uiDevotionalsUiState by devotionalsViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Today's",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.fetchVerseOfTheDay() }) {
                        Icon(
                            imageVector = Lucide.Calendar,
                            contentDescription = "Share"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            if (uiState.isLoading) {
                LoadingState()
            } else if (uiState.error != null) {
                ErrorState(
                    error = uiState.error!!,
                    onRetry = { viewModel.fetchVerseOfTheDay() }
                )
            } else {
                DevotionalsContent(
                    uiState = uiState,
                    onPlayAudio = { viewModel.playAudio() },
                    navController = navController,
                    uiDevotionalsUiState = uiDevotionalsUiState
                )
            }
        }
    }
}