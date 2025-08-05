package io.branch.view.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Book
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pause
import com.composables.icons.lucide.Play
import com.composables.icons.lucide.Share2
import io.branch.utils.audio.AudioState
import io.branch.utils.verse.VerseOfTheDay

@Composable
fun ReflectionCard(
    verse: VerseOfTheDay,
    audioState: AudioState,
    onPlayAudio: () -> Unit,
    onFavoriteClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onBibleClick: () -> Unit = {},
    onCardClick: () -> Unit = {},
    isAudioLoading: Boolean = false,
    isFavorited: Boolean = false,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()
    val cardBackground = MaterialTheme.colorScheme.surface
    val textPrimary = MaterialTheme.colorScheme.onSurface
    val accentColor = MaterialTheme.colorScheme.primary
    val borderColor = MaterialTheme.colorScheme.outline.copy(alpha = if (isDarkTheme) 0.7f else 0.3f)
    val iconColor = MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        onClick = onCardClick
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

                    Text(
                        text = "Thoughts on ${verse.reference}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = textPrimary
                    )
                }

                // Favorite icon
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorited) Lucide.Heart else Lucide.Heart,
                        contentDescription = "Favorite",
                        tint = if (isFavorited) accentColor else iconColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = verse.thoughts,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = textPrimary,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // Divider
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Action icons (share, bible)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onShareClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Lucide.Share2,
                            contentDescription = "Share",
                            tint = iconColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = onBibleClick,
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

                // Audio player button
                if (verse.audioUrl.isNotEmpty()) {
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

                        Button(
                            onClick = onPlayAudio,
                            enabled = !isAudioLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            ),
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Icon(
                                imageVector = if (audioState.isPlaying) Lucide.Pause else Lucide.Play,
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