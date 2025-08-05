package io.branch.utils.verse

data class VerseOfTheDay(
    val reference: String = "",
    val text: String = "",
    val audioUrl: String = "",
    val thoughts: String = "",
    val prayer: String = "",
    val translation: String = "KJV",
    val imageUrl: String = ""
)