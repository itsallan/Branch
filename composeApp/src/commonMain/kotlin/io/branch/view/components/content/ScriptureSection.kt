package io.branch.view.components.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.branch.utils.audio.AudioState
import io.branch.utils.dates.VerseWithDate
import io.branch.utils.dates.formatDateForDisplay
import io.branch.utils.dates.formatDateForUrl
import io.branch.utils.verse.VerseActions
import io.branch.utils.verse.VerseUIState
import io.branch.utils.verse.loadSingleVerse
import io.branch.view.components.cards.utils.LoadingCard
import io.branch.view.components.cards.utils.SwipeIndicator
import io.branch.view.screens.home.VerseViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun ScriptureContent(
    uiState: VerseUIState,
    actions: VerseActions,
    navController: NavController,
    viewModel: VerseViewModel,
    modifier: Modifier = Modifier
) {
    var verses by remember { mutableStateOf(listOf<VerseWithDate>()) }
    var currentIndex by remember { mutableStateOf(0) }
    var isPreloading by remember { mutableStateOf(false) }

    var verseAudioStates by remember { mutableStateOf(mapOf<String, AudioState>()) }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { verses.size }
    )

    LaunchedEffect(uiState.verse) {
        if (verses.isEmpty() && !uiState.isLoading && uiState.verse.text.isNotEmpty()) {
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            verses = listOf(
                VerseWithDate(
                    date = today,
                    verse = uiState.verse,
                    isToday = true
                )
            )

            launch {
                val yesterday = today.minus(DatePeriod(days = 1))
                verses = verses + loadSingleVerse(viewModel, yesterday)
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        currentIndex = pagerState.currentPage
        val shouldPreload = pagerState.currentPage == verses.size - 1

        if (shouldPreload && !isPreloading && verses.isNotEmpty()) {
            isPreloading = true
            val nextDate = verses.first().date.minus(DatePeriod(days = verses.size))

            launch {
                verses = verses + loadSingleVerse(viewModel, nextDate)
                isPreloading = false
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (verses.isNotEmpty()) {
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                contentPadding = PaddingValues(top = 34.dp),
                pageSpacing = 0.dp
            ) { page ->
                val verseWithDate = verses[page]

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        val displayText = if (verseWithDate.isToday) "Today" else formatDateForDisplay(verseWithDate.date)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(24.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                            )

                            Spacer(modifier = Modifier.width(8.dp))
//                            Box(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(16.dp),
//                            ) {
                                Text(
                                    text = displayText,
                                    style = MaterialTheme.typography.titleMedium,
                                    // color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.SemiBold
                                )
                          //  }
                        }
                    }

                    item {
                        when {
                            verseWithDate.isLoading -> LoadingCard()
                            verseWithDate.verse != null -> {
                                val dateKey = formatDateForUrl(verseWithDate.date)

                                val currentUiState = if (verseWithDate.isToday) {
                                    uiState
                                } else {
                                    VerseUIState(
                                        verse = verseWithDate.verse,
                                        audioState = verseAudioStates[dateKey] ?: AudioState(),
                                        isLoading = false,
                                        isAudioLoading = false,
                                        isFavorited = false,
                                        error = null
                                    )
                                }

                                val currentActions = if (verseWithDate.isToday) {
                                    actions
                                } else {
                                    VerseActions(
                                        onPlayAudio = {
                                            if (verseWithDate.verse.audioUrl.isNotEmpty()) {
                                                val currentAudioState = verseAudioStates[dateKey] ?: AudioState()
                                                if (currentAudioState.isPlaying) {
                                                    viewModel.pauseAudio()
                                                    verseAudioStates = verseAudioStates + (dateKey to currentAudioState.copy(isPlaying = false))
                                                } else {
                                                    verseAudioStates = verseAudioStates.mapValues { (_, state) ->
                                                        state.copy(isPlaying = false)
                                                    }

                                                    viewModel.playAudioForUrl(verseWithDate.verse.audioUrl)
                                                    verseAudioStates = verseAudioStates + (dateKey to currentAudioState.copy(isPlaying = true))
                                                }
                                            }
                                        },
                                        onFavoriteClick = {
                                            // Handle favorite for historical verses if needed
                                        },
                                        onShareClick = {
                                            // Handle share for historical verses if needed
                                        },
                                        onBibleClick = {
                                            // Handle bible click for historical verses if needed
                                        },
                                        onCardClick = {
                                            // Handle card click for historical verses if needed
                                        }
                                    )
                                }

                                VerseContent(
                                    uiState = currentUiState,
                                    actions = currentActions,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        } else {
            LoadingCard()
        }

        if (verses.size > 1) {
            SwipeIndicator(
                currentPage = currentIndex,
                totalPages = verses.size,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp)
            )
        }
    }
}