package io.branch.view.components.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.branch.data.model.ScriptureData
import io.branch.view.components.cards.PrayerCard
import io.branch.view.components.cards.ReflectionCard
import io.branch.view.components.cards.ScriptureCard
import io.branch.view.components.header.DayHeader

@Composable
fun ScriptureDaySection(
    scripture: ScriptureData,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DayHeader(
            dayAbbreviation = scripture.dayAbbreviation,
            date = scripture.date,
            isBookmarked = isBookmarked,
            onBookmarkClick = onBookmarkClick,
            onShareClick = onShareClick
        )

        ScriptureCard(scripture = scripture)

        ReflectionCard(themeColor = scripture.theme.colorSeed)

        PrayerCard(themeColor = scripture.theme.colorSeed)
    }
}