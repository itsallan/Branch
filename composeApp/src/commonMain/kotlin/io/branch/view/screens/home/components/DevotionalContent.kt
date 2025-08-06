package io.branch.view.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.composables.icons.lucide.Book
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pause
import com.composables.icons.lucide.Play
import com.composables.icons.lucide.Share
import io.branch.data.model.devotional.DevotionalCategory
import io.branch.utils.verse.UiState
import io.branch.view.screens.home.state.DevotionalsUiState

@Composable
fun DevotionalsContent(
    uiState: UiState,
    uiDevotionalsUiState: DevotionalsUiState,
    onPlayAudio: () -> Unit,
    isAudioLoading: Boolean = false,
    navController: NavController
) {
    val verseData = uiState.verseData
    val audioState = uiState.audioState

    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = MaterialTheme.colorScheme.background
    val cardBackground = MaterialTheme.colorScheme.surface
    val textPrimary = MaterialTheme.colorScheme.onSurface
    val accentColor = MaterialTheme.colorScheme.primary
    val borderColor =
        MaterialTheme.colorScheme.outline.copy(alpha = if (isDarkTheme) 0.7f else 0.3f)
    val iconColor = MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            DevotionalCard()
            Text(
                text = "Verse",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
            )

            Box {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBackground),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, borderColor),
                    onClick = {

                    }
                ) {
                    Column(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 20.dp,
                            bottom = 16.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .width(4.dp)
                                        .height(24.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(accentColor)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                // Verse reference
                                Text(
                                    text = verseData.reference,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = textPrimary
                                )
                            }

                            // Favorite icon
                            IconButton(
                                onClick = { /* Favorite action */ },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Lucide.Heart,
                                    contentDescription = "Favorite",
                                    tint = iconColor,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Verse text
                        Text(
                            text = verseData.text,
                            fontFamily = FontFamily.Serif,
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            color = textPrimary,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        // Action row with divider
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(
                                    onClick = { /* Share action */ },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Lucide.Share,
                                        contentDescription = "Share",
                                        tint = iconColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                IconButton(
                                    onClick = { /* Open in Bible action */ },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Lucide.Book,
                                        contentDescription = "Open in Bible",
                                        tint = iconColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            // Audio player button with loading indicator
                            if (verseData.audioUrl.isNotEmpty()) {
                                Box(
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isAudioLoading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(28.dp),
                                            color = accentColor,
                                            strokeWidth = 2.dp
                                        )
                                    }

                                    // Play/Pause button
                                    Button(
                                        onClick = onPlayAudio,
                                        enabled = !isAudioLoading,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary.copy(
                                                alpha = 0.7f
                                            ),
                                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                                                alpha = 0.5f
                                            ),
                                            disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                                alpha = 0.7f
                                            )
                                        ),
                                        shape = RoundedCornerShape(50),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        modifier = Modifier.height(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (audioState.isPlaying)
                                                Lucide.Pause else Lucide.Play,
                                            contentDescription = if (audioState.isPlaying) "Pause" else "Play",
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.size(14.dp)
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(
                                            text = if (audioState.isPlaying) "Pause" else "Listen",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            val excludedTitles = listOf("Bible Reading Plan")

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val filteredDevotionals = uiDevotionalsUiState.devotionals.filter { devotional ->
                    !excludedTitles.any { excluded ->
                        devotional.title.contains(excluded, ignoreCase = true)
                    }
                }

                val lifeInChristDevotionals = filteredDevotionals.filter {
                    it.category == DevotionalCategory.LIFE_IN_CHRIST
                }
                if (lifeInChristDevotionals.isNotEmpty()) {
                    DevotionalCategoryRow(
                        title = "Life in Christ",
                        devotionals = lifeInChristDevotionals,
                        navController = navController
                    )
                }

                val holySpiritDevotionals = filteredDevotionals.filter {
                    it.category == DevotionalCategory.HOLY_SPIRIT
                }
                if (holySpiritDevotionals.isNotEmpty()) {
                    DevotionalCategoryRow(
                        title = "Holy Spirit",
                        devotionals = holySpiritDevotionals,
                        navController = navController
                    )
                }

                val dailyEncouragementDevotionals = filteredDevotionals.filter {
                    it.category == DevotionalCategory.DAILY_ENCOURAGEMENT
                }
                if (dailyEncouragementDevotionals.isNotEmpty()) {
                    DevotionalCategoryRow(
                        title = "Daily Encouragement",
                        devotionals = dailyEncouragementDevotionals,
                        navController = navController
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}