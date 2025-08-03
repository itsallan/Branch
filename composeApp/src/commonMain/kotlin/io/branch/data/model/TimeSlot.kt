package io.branch.data.model

data class TimeSlot(
    val startTime: String,
    val endTime: String,
    val duration: String = "$startTime - $endTime"
)