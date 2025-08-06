package io.branch.view.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fleeksoft.ksoup.Ksoup
import io.branch.data.model.DevotionalResponse
import io.branch.utils.network.NetworkClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {
    private val _devotionalContent = MutableStateFlow(DevotionalResponse())
    val devotionalContent: StateFlow<DevotionalResponse> = _devotionalContent.asStateFlow()

    fun fetchDevotionalContent(websiteUrl: String) {
        _devotionalContent.value = DevotionalResponse(
            verse = _devotionalContent.value.verse,
            reference = _devotionalContent.value.reference,
            imageUrl = _devotionalContent.value.imageUrl,
            thoughtText = _devotionalContent.value.thoughtText,
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            try {
                val content = scrapeDevotionalContent(websiteUrl)
                _devotionalContent.value = content
            } catch (e: Exception) {
                _devotionalContent.value = DevotionalResponse(
                    verse = _devotionalContent.value.verse,
                    reference = _devotionalContent.value.reference,
                    imageUrl = _devotionalContent.value.imageUrl,
                    thoughtText = _devotionalContent.value.thoughtText,
                    isLoading = false,
                    error = "Failed to load devotional: ${e.message}"
                )
            }
        }
    }

    private suspend fun scrapeDevotionalContent(websiteUrl: String): DevotionalResponse {
        try {
            val secureUrl = if (websiteUrl.startsWith("http://")) {
                websiteUrl.replace("http://", "https://")
            } else {
                websiteUrl
            }

            println("Fetching content from: $secureUrl")
            val response = NetworkClient.httpClient.get(secureUrl)
            val htmlContent = response.bodyAsText()
            val doc = Ksoup.parse(htmlContent)

            val isAYearWithJesus = websiteUrl.contains("ayearwithjesus.com")
            val isPrayingWithPaul = websiteUrl.contains("praying_paul") || websiteUrl.contains("prayingwithpaul")

            println("Detected site format: ${
                when {
                    isAYearWithJesus -> "A Year With Jesus"
                    isPrayingWithPaul -> "Praying with Paul"
                    else -> "Heartlight"
                }
            }")

            var title = ""
            if (isAYearWithJesus) {
                title = doc.selectFirst("h1")?.text() ?: ""
            } else if (isPrayingWithPaul) {
                val pageTitle = doc.title()
                title = if (pageTitle.contains("Praying with Paul")) {
                    pageTitle.substringAfter("Praying with Paul: '").substringBefore("'")
                } else {
                    doc.selectFirst("div.a2a_kit")?.attr("data-a2a-title")?.substringAfter("Praying with Paul: '")?.substringBefore("'") ?: ""
                }
            } else {
                val pageTitle = doc.title()
                if (pageTitle.isNotEmpty()) {
                    title = pageTitle.substringBefore(" — ")
                } else {
                    val pathSegments = websiteUrl.split("/")
                    title = pathSegments.lastOrNull()?.substringBefore(".html") ?: ""
                }
            }
            println("Title: $title")

            var verse = ""
            var reference = ""

            if (isAYearWithJesus) {
                val noteContent = doc.selectFirst("div.note-verses-prayer")
                if (noteContent != null) {
                    verse = "Note from Jesus"
                    reference = ""
                }
            } else if (isPrayingWithPaul) {
                val referenceHeader = doc.selectFirst("h4")
                if (referenceHeader != null && referenceHeader.selectFirst("a.rtBibleRef") != null) {
                    reference = referenceHeader.text()

                    verse = "Prayer based on $reference"
                }
            } else {
                val leadWell = doc.selectFirst("div.lead.well")
                verse = when {
                    leadWell?.selectFirst("p") != null -> leadWell.selectFirst("p")?.text() ?: ""
                    else -> leadWell?.ownText() ?: ""
                }

                val leadRef = doc.selectFirst("div.lead-ref")
                reference = leadRef?.text()?.removePrefix("— ")?.trim() ?: ""
            }

            val headerImage = doc.selectFirst("div.subpage-head img.header-image, div.subpage-head img.responsive")?.attr("src") ?: ""

            val imageUrl = when {
                headerImage.isNotEmpty() -> headerImage
                else -> ""
            }
            println("Image URL: $imageUrl")

            val audioUrl = if (!isAYearWithJesus && !isPrayingWithPaul) {
                doc.selectFirst("audio#player source")?.attr("src") ?: ""
            } else {
                ""
            }
            println("Audio URL: $audioUrl")

            var thoughtText = ""
            var thoughtTitle = ""
            var prayerText = ""
            var prayerTitle = ""

            if (isAYearWithJesus) {
                thoughtTitle = "Note from Jesus"
                val noteContent = doc.selectFirst("div.note-verses-prayer")
                if (noteContent != null) {
                    thoughtText = noteContent.html()

                    thoughtText = thoughtText
                        .replace("<br>", "\n")
                        .replace("<br/>", "\n")
                        .replace("<br />", "\n")
                        .replace("<blockquote>", "\n\n")
                        .replace("</blockquote>", "\n\n")
                        .replace("<ol>", "\n")
                        .replace("</ol>", "\n")
                        .replace("<li>", "\n• ")
                        .replace("</li>", "")
                        .replace(Regex("<[^>]*>"), "")
                        .replace("&nbsp;", " ")
                        .trim()

                    println("Found A Year With Jesus content, length: ${thoughtText.length}")
                    println("First 100 chars: ${thoughtText.take(100)}")
                }
            } else if (isPrayingWithPaul) {
                prayerTitle = "Prayer"

                val prayerTextDiv = doc.selectFirst("div.prayer-text")
                if (prayerTextDiv != null) {
                    val sb = StringBuilder()
                    val paragraphs = prayerTextDiv.select("p")

                    for (p in paragraphs) {
                        sb.append(p.text())
                        sb.append("\n\n")
                    }

                    prayerText = sb.toString().trim()

                    thoughtTitle = ""
                    thoughtText = ""

                    println("Found Praying with Paul content, length: ${prayerText.length}")
                }
            } else {
                val possibleThoughtHeaders =
                    listOf("Thoughts on Today", "Key Thought", "Reflection", "Thought")
                val h4Elements = doc.select("h4")

                var thoughtHeader: com.fleeksoft.ksoup.nodes.Element? = null

                for (header in h4Elements) {
                    val headerText = header.text()
                    if (possibleThoughtHeaders.any { headerText.contains(it) }) {
                        thoughtHeader = header
                        thoughtTitle = headerText
                        break
                    }
                }

                if (thoughtHeader != null) {
                    val thoughtElement = doc.selectFirst("div.thought-text")

                    if (thoughtElement != null) {
                        thoughtText = thoughtElement.html()

                        thoughtText = thoughtText
                            .replace("<p>", "")
                            .replace("</p>", "\n\n")
                            .replace("<br>", "\n")
                            .replace("<br/>", "\n")
                            .replace("<br />", "\n")
                            .replace("<u>", "")
                            .replace("</u>", "")
                            .replace("<i>", "")
                            .replace("</i>", "")
                            .replace("<span class=\"alt\">", "")
                            .replace("<span class=\"small-caps\">", "")
                            .replace("</span>", "")
                            .replace("<ul>", "\n")
                            .replace("</ul>", "\n")
                            .replace("<li>", "\n• ")
                            .replace("</li>", "")
                            .replace("<blockquote>", "\n\n")
                            .replace("</blockquote>", "\n\n")
                            .replace(Regex("<div class=\"[^\"]*footnote[^\"]*\">[^<]*</div>"), "")
                            .replace(Regex("<[^>]*>"), "")
                            .replace("&nbsp;", " ")
                            .trim()
                    }
                }

                val possiblePrayerHeaders = listOf("Prayer", "My Prayer", "Today's Prayer")
                var prayerHeader: com.fleeksoft.ksoup.nodes.Element? = null

                for (header in h4Elements) {
                    val headerText = header.text()
                    if (possiblePrayerHeaders.any { headerText.contains(it) }) {
                        prayerHeader = header
                        prayerTitle = headerText
                        break
                    }
                }

                if (prayerHeader != null) {
                    val prayerTextDiv = doc.selectFirst("div.prayer-text")

                    if (prayerTextDiv != null) {
                        var prayerHtml = prayerTextDiv.html()

                        prayerText = prayerHtml
                            .replace("<div class=\"br\"></div>", "\n")
                            .replace("<br>", "\n")
                            .replace("<br/>", "\n")
                            .replace("<br />", "\n")
                            .replace("<p>", "")
                            .replace("</p>", "\n\n")
                            .replace(Regex("<[^>]*>"), "")
                            .replace("&nbsp;", " ")
                            .trim()
                            .replace(Regex("\n{3,}"), "\n\n")
                    }
                }
            }

            val relatedScriptures = mutableListOf<String>()
            val footnotes = mutableListOf<String>()

            if (isAYearWithJesus) {
                val scriptureRefs = doc.select("a.rtBibleRef")
                for (ref in scriptureRefs) {
                    val scriptureRef = ref.attr("data-reference")
                    if (scriptureRef.isNotEmpty()) {
                        relatedScriptures.add(scriptureRef)
                    }
                }
            } else if (isPrayingWithPaul) {
                val mainRef = doc.selectFirst("h4 a.rtBibleRef")
                if (mainRef != null) {
                    val scriptureRef = mainRef.attr("data-reference")
                    if (scriptureRef.isNotEmpty()) {
                        relatedScriptures.add(scriptureRef)
                    }
                }
            } else {
                val thoughtFootnotes = doc.select("div.tcfootnote, div.ghffootnote")
                for (footnote in thoughtFootnotes) {
                    footnotes.add(footnote.text())
                }

                val prayerNotes = doc.select("p.ghffootnote, p.tcfootnote")
                for (note in prayerNotes) {
                    footnotes.add(note.text())
                }

                val scriptureHeaders = listOf("Related Scripture", "Related Reading")
                val h4Elements = doc.select("h4")

                for (header in h4Elements) {
                    if (scriptureHeaders.any { header.text().contains(it) }) {
                        var scriptureList = header.nextElementSibling()
                        if (scriptureList?.tagName() == "ul") {
                            for (li in scriptureList.select("li")) {
                                relatedScriptures.add(li.text())
                            }
                        }
                        break
                    }
                }
            }

            println("Related scriptures: ${relatedScriptures.size}, Footnotes: ${footnotes.size}")

            var author = when {
                isAYearWithJesus -> "Jesus"
                isPrayingWithPaul -> "Eldon Degge"
                else -> {
                    var authorName = ""
                    val aboutSection = doc.select("div:contains(About This Devotional)").firstOrNull()
                    if (aboutSection != null) {
                        val paragraphs = aboutSection.select("p")
                        for (p in paragraphs) {
                            if (p.text().contains("written by")) {
                                val authorLink = p.selectFirst("a")
                                authorName = authorLink?.text() ?: ""
                                break
                            } else if (p.selectFirst("a") != null) {
                                authorName = p.selectFirst("a")?.text() ?: ""
                                break
                            }
                        }
                    }
                    authorName
                }
            }
            println("Author: $author")

            val relatedDevotionals = mutableListOf<Map<String, String>>()

            val devotionalWrappers = doc.select("div.cross-promo-wrapper")
            for (wrapper in devotionalWrappers) {
                val devoTitle = wrapper.selectFirst("div.cross-promo-title a")?.text() ?: continue
                val summary = wrapper.selectFirst("div.cross-promo-summary")?.text() ?: ""
                val link = wrapper.selectFirst("div.center a")?.attr("href") ?: ""
                val previewImage = wrapper.selectFirst("img.cross-promo-img")?.attr("src") ?: ""

                relatedDevotionals.add(
                    mapOf(
                        "title" to devoTitle,
                        "summary" to summary,
                        "link" to link,
                        "previewImage" to previewImage
                    )
                )
            }

            return DevotionalResponse(
                title = title,
                verse = verse,
                reference = reference,
                imageUrl = imageUrl,
                audioUrl = audioUrl,
                thoughtTitle = thoughtTitle,
                thoughtText = thoughtText,
                prayerTitle = prayerTitle,
                prayerText = prayerText,
                relatedScriptures = relatedScriptures,
                footnotes = footnotes,
                author = author,
                relatedDevotionals = relatedDevotionals,
                isLoading = false,
                error = null
            )
        } catch (e: Exception) {
            println("Error scraping devotional: ${e.message}")
            e.printStackTrace()

            return DevotionalResponse(
                isLoading = false,
                error = "Error loading devotional: ${e.message}"
            )
        }
    }

}