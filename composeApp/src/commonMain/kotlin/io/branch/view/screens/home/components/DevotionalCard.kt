package io.branch.view.screens.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.branch.utils.dates.shouldShowMorningDevotional
import io.branch.utils.network.getDevotionalUrl
import io.branch.view.screens.home.DevotionalContent
import io.branch.view.screens.home.state.scrapeDevotional
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun DevotionalCard() {
    val coroutineScope = rememberCoroutineScope()

    val devotionalContent = remember { mutableStateOf(DevotionalContent()) }
    val isLoading = remember { mutableStateOf(true) }

    val isMorningDevotional = remember { mutableStateOf(shouldShowMorningDevotional()) }

    val today = remember {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
    val selectedDate = remember { mutableStateOf(today) }

    fun loadDevotional(date: LocalDate, isAM: Boolean) {
        isLoading.value = true
        coroutineScope.launch {
            try {
                val devotionalUrl = getDevotionalUrl(date, isAM)
                devotionalContent.value = scrapeDevotional(devotionalUrl)
            } catch (e: Exception) {
                devotionalContent.value = DevotionalContent(
                    leadQuote = "ERROR",
                    reference = "",
                    morningThought = e.message ?: "Unknown error",
                    illustrationUrl = ""
                )
            } finally {
                isLoading.value = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadDevotional(selectedDate.value, isMorningDevotional.value)
    }

    if (isLoading.value) {
        // Show loading state
    } else if (devotionalContent.value.leadQuote == "NOT_FOUND" || devotionalContent.value.leadQuote == "ERROR") {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Error display component
        }
    } else {
        Text(
            text = if(isMorningDevotional.value) "Morning Devotion" else "Evening Devotion",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
        )
        VerseCard(
            verseText = devotionalContent.value.leadQuote,
            verseReference = devotionalContent.value.reference,
            translation = "KJV"
        )
    }
}