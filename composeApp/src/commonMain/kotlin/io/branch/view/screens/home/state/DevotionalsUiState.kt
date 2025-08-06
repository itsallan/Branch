package io.branch.view.screens.home.state

import io.branch.data.model.devotional.Devotional

data class DevotionalsUiState(
    val devotionals: List<Devotional> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)