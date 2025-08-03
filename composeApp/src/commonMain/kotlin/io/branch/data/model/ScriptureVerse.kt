package io.branch.data.model

data class ScriptureVerse(
    val reference: String,        // e.g., "Matthew 11:28"
    val text: String,            // The actual verse text
    val translation: String = "NIV"  // Bible translation
)