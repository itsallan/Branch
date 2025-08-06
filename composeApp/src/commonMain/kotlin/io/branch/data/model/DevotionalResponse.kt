package io.branch.data.model

data class DevotionalResponse(
    val title: String = "",
    val verse: String = "",
    val reference: String = "",
    val imageUrl: String = "",
    val audioUrl: String = "",
    val thoughtTitle: String = "",
    val thoughtText: String = "",
    val prayerTitle: String = "",
    val prayerText: String = "",
    val relatedScriptures: List<String> = emptyList(),
    val footnotes: List<String> = emptyList(),
    val author: String = "",
    val relatedDevotionals: List<Map<String, String>> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)