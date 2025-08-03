package io.branch.utils

import io.branch.data.model.ScriptureData
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun getTodayIndex(scriptures: List<ScriptureData>): Int {
    val today =  kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).dayOfWeek
    return scriptures.indexOfFirst { it.dayOfWeek == today }.takeIf { it != -1 } ?: 0
}