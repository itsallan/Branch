package io.branch.utils.verse

import io.branch.utils.dates.VerseWithDate
import io.branch.utils.dates.formatDateForDisplay
import io.branch.view.screens.home.VerseViewModel
import kotlinx.datetime.LocalDate

suspend fun loadSingleVerse(
    viewModel: VerseViewModel,
    date: LocalDate
): VerseWithDate {
    return try {
        val verse = viewModel.fetchVerseForDate(date)
        VerseWithDate(date = date, verse = verse)
    } catch (e: Exception) {
        VerseWithDate(
            date = date,
            verse = VerseOfTheDay(
                reference = "Error",
                text = "Could not load verse for ${formatDateForDisplay(date)}",
                audioUrl = "",
                thoughts = "",
                prayer = "",
                imageUrl = ""
            )
        )
    }
}