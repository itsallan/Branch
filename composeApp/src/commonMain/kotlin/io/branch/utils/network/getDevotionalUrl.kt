package io.branch.utils.network

import io.branch.utils.dates.formatDateForUrl
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun formatDateForUrl(date: LocalDate? = null, isAM: Boolean = true): String {
    val dateToFormat = date ?: Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date

    val month = dateToFormat.monthNumber.toString().padStart(2, '0')
    val day = dateToFormat.dayOfMonth.toString().padStart(2, '0')
    val timeOfDay = if (isAM) "am" else "pm"

    return "$month$day-$timeOfDay"
}

fun getDevotionalUrl(date: LocalDate? = null, isAM: Boolean = true): String {
    val baseUrl = "https://www.heartlight.org/spurgeon/"
    val formattedDate = formatDateForUrl(date, isAM)
    return "${baseUrl}${formattedDate}.html"
}