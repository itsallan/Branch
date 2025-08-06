package io.branch.utils.dates

import io.branch.utils.verse.VerseOfTheDay
import kotlinx.datetime.LocalDate

data class VerseWithDate(
    val date: LocalDate,
    val verse: VerseOfTheDay? = null,
    val isLoading: Boolean = false,
    val isToday: Boolean = false
)