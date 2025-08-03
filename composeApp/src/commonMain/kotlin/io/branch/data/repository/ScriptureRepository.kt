package io.branch.data.repository

import androidx.compose.ui.graphics.Color
import io.branch.data.model.DailySchedule
import io.branch.data.model.ScriptureData
import io.branch.data.model.ScriptureTheme
import io.branch.data.model.ScriptureVerse
import io.branch.data.model.TimeSlot
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object ScriptureRepository {
    @OptIn(ExperimentalTime::class)
    fun getWeeklyScriptures(weekOffset: Int = 0): List<ScriptureData> {
        // Get current date information
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val currentMonth = getMonthName(now.month)
        val currentYear = now.year

        // Calculate base date (start of current week)
        val today = now.date
        val dayOfWeek = now.dayOfWeek
        val daysFromSunday = when(dayOfWeek) {
            DayOfWeek.SUNDAY -> 0
            DayOfWeek.MONDAY -> 1
            DayOfWeek.TUESDAY -> 2
            DayOfWeek.WEDNESDAY -> 3
            DayOfWeek.THURSDAY -> 4
            DayOfWeek.FRIDAY -> 5
            DayOfWeek.SATURDAY -> 6
            else -> 0
        }

        val weekStartDate = today.minus(DatePeriod(days = daysFromSunday))

        return listOf(
            ScriptureData(
                id = "sunday_${weekOffset}",
                dayOfWeek = DayOfWeek.SUNDAY,
                date = weekStartDate.dayOfMonth,
                month = currentMonth,
                year = currentYear,
                verse = ScriptureVerse(
                    reference = "Matthew 11:28",
                    text = "Come to me, all you who are weary and burdened, and I will give you rest."
                ),
                theme = ScriptureTheme(
                    name = "Rest & Renewal",
                    description = "Finding peace in God's presence",
                    colorSeed = Color(0xFF9C27B0),
                    emoji = "🌙"
                ),
                schedule = DailySchedule(
                    devotionTime = TimeSlot("08:00", "09:00"),
                    reflectionTime = TimeSlot("09:00", "09:15"),
                    prayerTime = TimeSlot("09:15", "09:30"),
                    location = "Morning Devotion"
                )
            ),
            ScriptureData(
                id = "monday_${weekOffset}",
                dayOfWeek = DayOfWeek.MONDAY,
                date = weekStartDate.plus(DatePeriod(days = 1)).dayOfMonth,
                month = currentMonth,
                year = currentYear,
                verse = ScriptureVerse(
                    reference = "Joshua 1:9",
                    text = "Have I not commanded you? Be strong and courageous. Do not be afraid; do not be discouraged, for the Lord your God will be with you wherever you go."
                ),
                theme = ScriptureTheme(
                    name = "Courage & Strength",
                    description = "Bold faith for new beginnings",
                    colorSeed = Color(0xFF2196F3),
                    emoji = "⚡"
                ),
                schedule = DailySchedule(
                    devotionTime = TimeSlot("07:30", "08:30"),
                    reflectionTime = TimeSlot("08:30", "08:45"),
                    prayerTime = TimeSlot("08:45", "09:00"),
                    location = "Daily Scripture"
                )
            ),
            ScriptureData(
                id = "tuesday_${weekOffset}",
                dayOfWeek = DayOfWeek.TUESDAY,
                date = weekStartDate.plus(DatePeriod(days = 2)).dayOfMonth,
                month = currentMonth,
                year = currentYear,
                verse = ScriptureVerse(
                    reference = "Philippians 4:13",
                    text = "I can do all this through him who gives me strength."
                ),
                theme = ScriptureTheme(
                    name = "Divine Strength",
                    description = "Power beyond our limitations",
                    colorSeed = Color(0xFF4CAF50),
                    emoji = "💪"
                ),
                schedule = DailySchedule(
                    devotionTime = TimeSlot("07:30", "08:30"),
                    reflectionTime = TimeSlot("08:30", "08:45"),
                    prayerTime = TimeSlot("08:45", "09:00"),
                    location = "Daily Scripture"
                )
            ),
            ScriptureData(
                id = "wednesday_${weekOffset}",
                dayOfWeek = DayOfWeek.WEDNESDAY,
                date = weekStartDate.plus(DatePeriod(days = 3)).dayOfMonth,
                month = currentMonth,
                year = currentYear,
                verse = ScriptureVerse(
                    reference = "Jeremiah 29:11",
                    text = "For I know the plans I have for you, declares the Lord, plans to prosper you and not to harm you, to give you hope and a future."
                ),
                theme = ScriptureTheme(
                    name = "Hope & Future",
                    description = "Trusting God's perfect plan",
                    colorSeed = Color(0xFFFF9800),
                    emoji = "✨"
                ),
                schedule = DailySchedule(
                    devotionTime = TimeSlot("07:30", "08:30"),
                    reflectionTime = TimeSlot("08:30", "08:45"),
                    prayerTime = TimeSlot("08:45", "09:00"),
                    location = "Daily Scripture"
                )
            ),
            ScriptureData(
                id = "thursday_${weekOffset}",
                dayOfWeek = DayOfWeek.THURSDAY,
                date = weekStartDate.plus(DatePeriod(days = 4)).dayOfMonth,
                month = currentMonth,
                year = currentYear,
                verse = ScriptureVerse(
                    reference = "Psalm 46:10",
                    text = "Be still, and know that I am God; I will be exalted among the nations, I will be exalted in the earth."
                ),
                theme = ScriptureTheme(
                    name = "Peace & Stillness",
                    description = "Finding calm in God's presence",
                    colorSeed = Color(0xFF009688),
                    emoji = "🕊️"
                ),
                schedule = DailySchedule(
                    devotionTime = TimeSlot("07:30", "08:30"),
                    reflectionTime = TimeSlot("08:30", "08:45"),
                    prayerTime = TimeSlot("08:45", "09:00"),
                    location = "Daily Scripture"
                )
            ),
            ScriptureData(
                id = "friday_${weekOffset}",
                dayOfWeek = DayOfWeek.FRIDAY,
                date = weekStartDate.plus(DatePeriod(days = 5)).dayOfMonth,
                month = currentMonth,
                year = currentYear,
                verse = ScriptureVerse(
                    reference = "Romans 8:28",
                    text = "And we know that in all things God works for the good of those who love him, who have been called according to his purpose."
                ),
                theme = ScriptureTheme(
                    name = "Faith & Purpose",
                    description = "Trusting in God's greater plan",
                    colorSeed = Color(0xFFE91E63),
                    emoji = "🔥"
                ),
                schedule = DailySchedule(
                    devotionTime = TimeSlot("07:30", "08:30"),
                    reflectionTime = TimeSlot("08:30", "08:45"),
                    prayerTime = TimeSlot("08:45", "09:00"),
                    location = "Daily Scripture"
                )
            ),
            ScriptureData(
                id = "saturday_${weekOffset}",
                dayOfWeek = DayOfWeek.SATURDAY,
                date = weekStartDate.plus(DatePeriod(days = 6)).dayOfMonth,
                month = currentMonth,
                year = currentYear,
                verse = ScriptureVerse(
                    reference = "Psalm 118:24",
                    text = "This is the day the Lord has made; we will rejoice and be glad in it."
                ),
                theme = ScriptureTheme(
                    name = "Joy & Gratitude",
                    description = "Celebrating God's faithfulness",
                    colorSeed = Color(0xFFFFEB3B),
                    emoji = "☀️"
                ),
                schedule = DailySchedule(
                    devotionTime = TimeSlot("08:00", "09:00"),
                    reflectionTime = TimeSlot("09:00", "09:15"),
                    prayerTime = TimeSlot("09:15", "09:30"),
                    location = "Weekend Reflection"
                )
            )
        )
    }

    // Helper function to get month name
    private fun getMonthName(month: Month): String {
        return when(month) {
            Month.JANUARY -> "January"
            Month.FEBRUARY -> "February"
            Month.MARCH -> "March"
            Month.APRIL -> "April"
            Month.MAY -> "May"
            Month.JUNE -> "June"
            Month.JULY -> "July"
            Month.AUGUST -> "August"
            Month.SEPTEMBER -> "September"
            Month.OCTOBER -> "October"
            Month.NOVEMBER -> "November"
            Month.DECEMBER -> "December"
            else -> "Unknown"
        }
    }

    @OptIn(ExperimentalTime::class)
    fun getTodaysScripture(): ScriptureData? {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).dayOfWeek
        return getWeeklyScriptures().find { it.dayOfWeek == today }
    }

    fun getScriptureByDay(dayOfWeek: DayOfWeek): ScriptureData? {
        return getWeeklyScriptures().find { it.dayOfWeek == dayOfWeek }
    }
}