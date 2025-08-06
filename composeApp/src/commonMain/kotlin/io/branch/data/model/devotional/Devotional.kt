package io.branch.data.model.devotional

data class Devotional(
    val id: String,
    val title: String,
    val description: String,
    val category: DevotionalCategory,
    val imageUrl: String,
    val websiteUrl: String,
    val verse: String = "",
    val reference: String = ""
)