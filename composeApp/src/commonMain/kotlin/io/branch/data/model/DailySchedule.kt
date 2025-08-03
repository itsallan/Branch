package io.branch.data.model

data class DailySchedule(
    val devotionTime: TimeSlot,
    val reflectionTime: TimeSlot,
    val prayerTime: TimeSlot,
    val location: String = "Personal Study"
)