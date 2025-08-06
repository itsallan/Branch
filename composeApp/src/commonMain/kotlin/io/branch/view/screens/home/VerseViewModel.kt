package io.branch.view.screens.home

import com.fleeksoft.ksoup.Ksoup
import io.branch.data.audio.AudioPlayer
import io.branch.utils.audio.AudioState
import io.branch.utils.dates.VerseWithDate
import io.branch.utils.network.NetworkClient
import io.branch.utils.verse.UiState
import io.branch.utils.verse.VerseOfTheDay
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class VerseViewModel(
    private val audioPlayer: AudioPlayer,
    private val coroutineScope: CoroutineScope
) {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var progressUpdateJob: Job? = null

    init {
        fetchVerseOfTheDay()
    }

    fun fetchVerseOfTheDay() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        coroutineScope.launch {
            try {
                val result = scrapeVerseOfTheDay()
                _uiState.value = _uiState.value.copy(
                    verseData = result,
                    isLoading = false
                )
                resetAudioPlayer()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error loading verse: ${e.message}"
                )
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun scrapeVerseOfTheDay(): VerseOfTheDay {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val dateString = formatDateForUrl(today)

        val url = "https://www.heartlight.org/cgi-shl/todaysverse.cgi?day=${dateString}&ver=niv"

        try {
            val response = NetworkClient.httpClient.get(url)
            val htmlContent = response.bodyAsText()

            val doc = Ksoup.parse(htmlContent)

            var verseReference = ""
            var verseText = ""
            var audioUrl = ""
            var thoughts = ""
            var prayer = ""
            var imageUrl = ""

            // Extract verse reference from the page title/header
            doc.selectFirst(".subpage-head h1 span.h1-devo")?.let { titleElement ->
                val fullTitle = titleElement.parent()?.text() ?: ""
                val referenceMatch = Regex("Today's Verse:\\s*(.+)").find(fullTitle)
                if (referenceMatch != null) {
                    verseReference = referenceMatch.groupValues[1].trim()
                }
            }

            // Extract verse text from the lead well section
            doc.selectFirst("div.lead.well")?.let { verseElement ->
                val fullText = verseElement.ownText()
                if (fullText.isNotEmpty()) {
                    verseText = fullText.trim()
                }
            }

            // Extract audio URL
            doc.selectFirst("audio#player source[type=\"audio/mpeg\"]")?.let { audioElement ->
                audioUrl = audioElement.attr("src")
                // Make sure it's a complete URL
                if (audioUrl.startsWith("/")) {
                    audioUrl = "https://www.heartlight.org$audioUrl"
                }
            }

            // Extract thoughts section
            doc.selectFirst("div.thought-text")?.let { thoughtElement ->
                thoughts = thoughtElement.text().trim()
            }

            // Extract prayer section
            doc.selectFirst("div.prayer-text")?.let { prayerElement ->
                prayer = prayerElement.text().trim()
            }

            // Extract header image URL
            doc.selectFirst(".subpage-head .header-image-bar img.header-image")?.let { imgElement ->
                imageUrl = imgElement.attr("src")
                if (imageUrl.startsWith("//")) {
                    imageUrl = "https:$imageUrl"
                } else if (imageUrl.startsWith("/")) {
                    imageUrl = "https://www.heartlight.org$imageUrl"
                }
            }


            if (imageUrl.isEmpty()) {
                doc.selectFirst("img[title*=\"Illustration of\"]")?.let { illustrationImg ->
                    imageUrl = illustrationImg.attr("src")
                    if (imageUrl.startsWith("//")) {
                        imageUrl = "https:$imageUrl"
                    } else if (imageUrl.startsWith("/")) {
                        imageUrl = "https://www.heartlight.org$imageUrl"
                    }
                }
            }

            return VerseOfTheDay(
                reference = verseReference,
                text = verseText,
                audioUrl = audioUrl,
                thoughts = thoughts,
                prayer = prayer,
                imageUrl = imageUrl
            )
        } catch (e: Exception) {
            return VerseOfTheDay(
                reference = "Error",
                text = "Could not load verse for today: ${e.message}",
                audioUrl = "",
                thoughts = "",
                prayer = "",
                imageUrl = ""
            )
        }
    }

    private fun formatDateForUrl(date: LocalDate): String {
        val year = date.year.toString()
        val month = date.month.number.toString().padStart(2, '0')
        val day = date.day.toString().padStart(2, '0')
        return "$year$month$day"
    }
    suspend fun fetchVerseForDate(date: LocalDate): VerseOfTheDay {
        val dateString = formatDateForUrl(date)
        val url = "https://www.heartlight.org/cgi-shl/todaysverse.cgi?day=${dateString}&ver=niv"

        println("Fetching verse for date: $dateString")
        try {
            val response = NetworkClient.httpClient.get(url)
            val htmlContent = response.bodyAsText()

            val doc = Ksoup.parse(htmlContent)

            var verseReference = ""
            var verseText = ""
            var audioUrl = ""
            var thoughts = ""
            var prayer = ""
            var imageUrl = ""

            // Same parsing logic as scrapeVerseOfTheDay()
            doc.selectFirst(".subpage-head h1 span.h1-devo")?.let { titleElement ->
                val fullTitle = titleElement.parent()?.text() ?: ""
                val referenceMatch = Regex("Today's Verse:\\s*(.+)").find(fullTitle)
                if (referenceMatch != null) {
                    verseReference = referenceMatch.groupValues[1].trim()
                }
            }

            doc.selectFirst("div.lead.well")?.let { verseElement ->
                val fullText = verseElement.ownText()
                if (fullText.isNotEmpty()) {
                    verseText = fullText.trim()
                }
            }

            doc.selectFirst("audio#player source[type=\"audio/mpeg\"]")?.let { audioElement ->
                audioUrl = audioElement.attr("src")
                if (audioUrl.startsWith("/")) {
                    audioUrl = "https://www.heartlight.org$audioUrl"
                }
            }

            doc.selectFirst("div.thought-text")?.let { thoughtElement ->
                thoughts = thoughtElement.text().trim()
            }

            doc.selectFirst("div.prayer-text")?.let { prayerElement ->
                prayer = prayerElement.text().trim()
            }

            doc.selectFirst(".subpage-head .header-image-bar img.header-image")?.let { imgElement ->
                imageUrl = imgElement.attr("src")
                if (imageUrl.startsWith("//")) {
                    imageUrl = "https:$imageUrl"
                } else if (imageUrl.startsWith("/")) {
                    imageUrl = "https://www.heartlight.org$imageUrl"
                }
            }

            // Fallback for illustration image
            if (imageUrl.isEmpty()) {
                doc.selectFirst("img[title*=\"Illustration of\"]")?.let { illustrationImg ->
                    imageUrl = illustrationImg.attr("src")
                    if (imageUrl.startsWith("//")) {
                        imageUrl = "https:$imageUrl"
                    } else if (imageUrl.startsWith("/")) {
                        imageUrl = "https://www.heartlight.org$imageUrl"
                    }
                }
            }

            return VerseOfTheDay(
                reference = verseReference,
                text = verseText,
                audioUrl = audioUrl,
                thoughts = thoughts,
                prayer = prayer,
                imageUrl = imageUrl
            )
        } catch (e: Exception) {
            return VerseOfTheDay(
                reference = "Error",
                text = "Could not load verse for: ${e.message}",
                audioUrl = "",
                thoughts = "",
                prayer = "",
                imageUrl = ""
            )
        }
    }

    // Method to fetch multiple verses for date range
    suspend fun fetchVersesForDateRange(dates: List<LocalDate>): List<VerseWithDate> {
        return dates.map { date ->
            try {
                val verse = fetchVerseForDate(date)
                VerseWithDate(date = date, verse = verse)
            } catch (e: Exception) {
                VerseWithDate(
                    date = date,
                    verse = VerseOfTheDay(
                        reference = "Error",
                        text = "Could not load verse for"
                    )
                )
            }
        }
    }

    fun playAudioForUrl(audioUrl: String) {
        if (audioUrl.isEmpty()) return

        try {
            if (audioPlayer.isPlaying()) {
                pauseAudio()
                return
            }

            audioPlayer.play(audioUrl)
            updateAudioState(isPlaying = true)
            startProgressUpdates()
        } catch (e: Exception) {
            updateAudioState(isPlaying = false)
            _uiState.value = _uiState.value.copy(
                error = "Error playing audio: ${e.message}"
            )
        }
    }

    fun playAudio() {
        val audioUrl = _uiState.value.verseData.audioUrl
        if (audioUrl.isEmpty()) return

        try {
            if (_uiState.value.audioState.isPlaying) {
                pauseAudio()
                return
            }

            audioPlayer.play(audioUrl)
            updateAudioState(isPlaying = true)
            startProgressUpdates()
        } catch (e: Exception) {
            updateAudioState(isPlaying = false)
            _uiState.value = _uiState.value.copy(
                error = "Error playing audio: ${e.message}"
            )
        }
    }

    fun pauseAudio() {
        if (audioPlayer.isPlaying()) {
            audioPlayer.pause()
            updateAudioState(isPlaying = false)
            stopProgressUpdates()
        }
    }

    private fun resetAudioPlayer() {
        stopProgressUpdates()
        audioPlayer.release()
        updateAudioState(isPlaying = false, progress = 0f)
    }

    private fun startProgressUpdates() {
        stopProgressUpdates()

        progressUpdateJob = coroutineScope.launch {
            while (isActive && audioPlayer.isPlaying()) {
                val duration = audioPlayer.getDuration()
                val currentPosition = audioPlayer.getCurrentPosition()
                val progress = if (duration > 0) currentPosition.toFloat() / duration else 0f

                updateAudioState(
                    isPlaying = true,
                    progress = progress,
                    currentPosition = formatTime(currentPosition),
                    duration = formatTime(duration)
                )

                delay(100)
            }
        }
    }

    private fun stopProgressUpdates() {
        progressUpdateJob?.cancel()
        progressUpdateJob = null
    }

    private fun updateAudioState(
        isPlaying: Boolean = _uiState.value.audioState.isPlaying,
        progress: Float = _uiState.value.audioState.progress,
        currentPosition: String = _uiState.value.audioState.currentPosition,
        duration: String = _uiState.value.audioState.duration
    ) {
        _uiState.value = _uiState.value.copy(
            audioState = AudioState(
                isPlaying = isPlaying,
                progress = progress,
                currentPosition = currentPosition,
                duration = duration
            )
        )
    }

    private fun formatTime(milliseconds: Long): String {
        val minutes = milliseconds / 60000
        val seconds = (milliseconds % 60000) / 1000
        return "${minutes}:${seconds.toString().padStart(2, '0')}"
    }

    fun onCleared() {
        resetAudioPlayer()
    }
}