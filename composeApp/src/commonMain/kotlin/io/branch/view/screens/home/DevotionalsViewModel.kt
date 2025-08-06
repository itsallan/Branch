package io.branch.view.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fleeksoft.ksoup.Ksoup
import io.branch.data.model.devotional.Devotional
import io.branch.data.model.devotional.DevotionalCategory
import io.branch.utils.network.NetworkClient
import io.branch.view.screens.home.state.DevotionalsUiState
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DevotionalsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DevotionalsUiState())
    val uiState: StateFlow<DevotionalsUiState> = _uiState.asStateFlow()

    init {
        fetchDevotionals()
    }

    fun fetchDevotionals() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val devotionals = scrapeDevotionals()
                _uiState.value = _uiState.value.copy(
                    devotionals = devotionals,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load devotionals: ${e.message}"
                )
            }
        }
    }

    private suspend fun scrapeDevotionals(): List<Devotional> {
        val url = "https://www.heartlight.org/devotionals/"
        val response = NetworkClient.httpClient.get(url)
        val htmlContent = response.bodyAsText()

        val doc = Ksoup.parse(htmlContent)
        val devotionals = mutableListOf<Devotional>()

        // Find all devotional teasers
        val devotionalTeasers = doc.select("div.devo-teaser")

        for (teaser in devotionalTeasers) {
            try {
                // Get the title
                val titleElement = teaser.selectFirst("div.devo-teaser-title")
                val title = titleElement?.text() ?: continue

                // Get the description
                val descriptionElement = teaser.selectFirst("div.devo-teaser-summary")
                val description =
                    descriptionElement?.text()?.replace("data-owner=\"balance-text\"", "")?.trim()
                        ?: ""

                // Get the website URL
                val linkElement = teaser.selectFirst("a")
                val websiteUrl = if (linkElement?.attr("href")?.startsWith("http") == true) {
                    linkElement.attr("href")
                } else {
                    "https://www.heartlight.org${linkElement?.attr("href") ?: ""}"
                }

                // Get the image URL
                val imageElement = teaser.selectFirst("img.devo-teaser-img")
                val imageUrl = imageElement?.attr("src") ?: ""

                // Get sample verse and reference
                val referenceElement = teaser.selectFirst("div.devo-teaser-ref")
                val reference = referenceElement?.text()?.trim() ?: ""

                val scriptureElement = teaser.selectFirst("div.devo-teaser-scripture")
                val sampleVerse =
                    scriptureElement?.text()?.replace("data-owner=\"balance-text\"", "")?.trim()
                        ?: ""

                // Determine category
                val category = when {
                    title.contains("Jesus") || title == "What Jesus Did!" || title == "A Year with Jesus" || title == "Together in Christ" ->
                        DevotionalCategory.LIFE_IN_CHRIST

                    title.contains("Holy Fire") || title.contains("Spiritual Warfare") || title == "Unstoppable!" ->
                        DevotionalCategory.HOLY_SPIRIT

                    else ->
                        DevotionalCategory.DAILY_ENCOURAGEMENT
                }

                val id = title.lowercase().replace(Regex("[^a-z0-9]"), "-")

                devotionals.add(
                    Devotional(
                        id = id,
                        title = title,
                        description = description,
                        category = category,
                        imageUrl = imageUrl,
                        websiteUrl = websiteUrl,
                        verse = sampleVerse,
                        reference = reference
                    )
                )
            } catch (e: Exception) {
                continue
            }
        }

        return devotionals
    }
}

data class DevotionalContent(
    val leadQuote: String = "",
    val reference: String = "",
    val morningThought: String = "",
    val illustrationUrl: String = ""
)