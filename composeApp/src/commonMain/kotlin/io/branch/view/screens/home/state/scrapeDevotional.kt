package io.branch.view.screens.home.state

import com.fleeksoft.ksoup.Ksoup
import io.branch.utils.network.NetworkClient
import io.branch.view.screens.home.DevotionalContent
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

suspend fun scrapeDevotional(url: String): DevotionalContent {
    return try {
        val response = NetworkClient.httpClient.get(url)
        val htmlContent = response.bodyAsText()
        val doc = Ksoup.parse(htmlContent)

        val leadQuoteElement = doc.selectFirst("div.lead.well")
        val leadQuote = leadQuoteElement?.ownText() ?: ""

        val referenceElement = doc.selectFirst("div.lead-ref")
        val reference = referenceElement?.text() ?: ""

        val thoughtElement = doc.selectFirst("div.thought-text")
        val morningThought = thoughtElement?.text() ?: ""

        val illustrationElement = doc.select("h4:contains(Illustration) + img")
        val illustrationUrl = illustrationElement.attr("src")

        DevotionalContent(
            leadQuote = leadQuote,
            reference = reference,
            morningThought = morningThought,
            illustrationUrl = illustrationUrl
        )
    } catch (e: Exception) {
        e.printStackTrace()
        DevotionalContent(
            leadQuote = "ERROR",
            reference = "",
            morningThought = e.message ?: "Unknown error",
            illustrationUrl = ""
        )
    }
}