package io.branch.view.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Lucide
import io.branch.data.audio.PlatformAudioPlayer
import io.branch.utils.verse.VerseActions
import io.branch.utils.verse.VerseUIState
import io.branch.view.components.content.ScriptureContent
import io.branch.view.components.header.AppHeader
import io.branch.view.components.shared.HomeIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun HomeScreen(
    navController: NavController
) {
    val coroutineScope = remember { CoroutineScope(Dispatchers.Main) }
    val audioPlayer = remember { PlatformAudioPlayer() }
    val viewModel = remember { VerseViewModel(audioPlayer, coroutineScope) }
    var isVerseFavorited by remember { mutableStateOf(false) }

    val verseUiState by viewModel.uiState.collectAsState()

    val today = remember {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }
    val dayAbbreviation = when (today.dayOfWeek) {
        DayOfWeek.SUNDAY -> "Sun"
        DayOfWeek.MONDAY -> "Mon"
        DayOfWeek.TUESDAY -> "Tue"
        DayOfWeek.WEDNESDAY -> "Wed"
        DayOfWeek.THURSDAY -> "Thu"
        DayOfWeek.FRIDAY -> "Fri"
        DayOfWeek.SATURDAY -> "Sat"
        else -> "???"
    }

    val uiState = VerseUIState(
        verse = verseUiState.verseData,
        audioState = verseUiState.audioState,
        isLoading = verseUiState.isLoading,
        isAudioLoading = false,
        isFavorited = isVerseFavorited,
        error = verseUiState.error
    )

    val actions = VerseActions(
        onPlayAudio = { viewModel.playAudio() },
        onCardClick = {
            // Handle verse card click
        },
        onFavoriteClick = {
            isVerseFavorited = !isVerseFavorited
        },
        onShareClick = {
            // Handle verse share
        },
        onBibleClick = {
            // Handle open in bible
        }
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Branches",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "John 15:5",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            //fontWeight = FontWeight.Medium,
                            fontSize = 10.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.fetchVerseOfTheDay() }) {
                        Icon(
                            imageVector = Lucide.Calendar,
                            contentDescription = "Share"
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            ScriptureContent(
                uiState = uiState,
                actions = actions,
                navController = navController,
                modifier = Modifier.weight(1f),
                viewModel = viewModel,
            )
        }
    }
}