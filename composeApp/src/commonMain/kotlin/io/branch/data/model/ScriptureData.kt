package io.branch.data.model

import kotlinx.datetime.DayOfWeek

data class ScriptureData(
    val id: String,
    val dayOfWeek: DayOfWeek,
    val date: Int,
    val month: String,
    val year: Int,
    val verse: ScriptureVerse,
    val theme: ScriptureTheme,
    val schedule: DailySchedule
) {
    // Computed properties
    val dayAbbreviation: String get() = when(dayOfWeek) {
        DayOfWeek.SUNDAY -> "Sun"
        DayOfWeek.MONDAY -> "Mon"
        DayOfWeek.TUESDAY -> "Tue"
        DayOfWeek.WEDNESDAY -> "Wed"
        DayOfWeek.THURSDAY -> "Thu"
        DayOfWeek.FRIDAY -> "Fri"
        DayOfWeek.SATURDAY -> "Sat"
        else -> "???"
    }
    val dayFullName: String get() = when(dayOfWeek) {
        DayOfWeek.SUNDAY -> "Sunday"
        DayOfWeek.MONDAY -> "Monday"
        DayOfWeek.TUESDAY -> "Tuesday"
        DayOfWeek.WEDNESDAY -> "Wednesday"
        DayOfWeek.THURSDAY -> "Thursday"
        DayOfWeek.FRIDAY -> "Friday"
        DayOfWeek.SATURDAY -> "Saturday"
        else -> "Unknown"
    }
    val dateDisplay: String get() = "$dayAbbreviation $date"
}