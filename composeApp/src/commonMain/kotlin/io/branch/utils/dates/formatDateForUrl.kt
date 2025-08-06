package io.branch.utils.dates

import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

fun formatDateForUrl(date: LocalDate): String {
    val year = date.year.toString()
    val month = date.month.number.toString().padStart(2, '0')
    val day = date.day.toString().padStart(2, '0')
    return "$year$month$day"
}