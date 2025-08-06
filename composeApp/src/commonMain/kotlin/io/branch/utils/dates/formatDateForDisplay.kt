package io.branch.utils.dates

import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

fun formatDateForDisplay(date: LocalDate): String {
    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    return "${months[date.month.number - 1]} ${date.day}, ${date.year}"
}