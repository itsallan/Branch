package io.branch.data.model

import androidx.compose.ui.graphics.Color

data class ScriptureTheme(
    val name: String,
    val description: String,
    val colorSeed: Color,
    val emoji: String = "📖"
)