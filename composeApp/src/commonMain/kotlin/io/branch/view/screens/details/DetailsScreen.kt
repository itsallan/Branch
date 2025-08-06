package io.branch.view.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.composables.icons.lucide.Bookmark
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pause
import com.composables.icons.lucide.Play
import com.composables.icons.lucide.Share
import io.branch.navigation.routes.DevotionalDetailRoute
import io.branch.view.components.cards.MagazineSection
import io.branch.view.screens.details.components.ErrorState
import io.branch.view.screens.details.components.ScriptureCard

@Composable
fun DetailsScreen(
    data: DevotionalDetailRoute,
    navController: NavController
) {
    val viewModel = remember { DetailsViewModel() }
    val content by viewModel.devotionalContent.collectAsState()
    val websiteUrl = data.link
    val scrollState = rememberScrollState()

    var isAudioPlaying by remember { mutableStateOf(false) }

    LaunchedEffect(websiteUrl) {
        viewModel.fetchDevotionalContent(websiteUrl)
    }

    Scaffold(
        floatingActionButton = {

        },
        content = { paddingValues ->
            when {
                content.isLoading -> {
                  //  LoadingState()
                }

                content.error != null -> {
                    ErrorState(
                        errorMessage = content.error,
                        onRetry = { viewModel.fetchDevotionalContent(websiteUrl) }
                    )
                }

                else -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (content.imageUrl.isNotEmpty()) {
                            AsyncImage(
                                model = content.imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(380.dp)
                                    .blur(radius = 4.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(380.dp)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Black.copy(alpha = 0.3f),
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                                MaterialTheme.colorScheme.background.copy(alpha = 1.9f)
                                            )
                                        )
                                    )
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(380.dp)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                                MaterialTheme.colorScheme.background
                                            )
                                        )
                                    )
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                        ) {
                            // Header Content
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 120.dp,
                                        start = 24.dp,
                                        end = 24.dp,
                                        bottom = 32.dp // Increased bottom padding
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = content.title,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 28.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = content.reference,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 4.dp
                                        ),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }

                            Column(modifier = Modifier.fillMaxWidth()) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 24.dp)
                                        .offset(y = (-32).dp),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 16.dp
                                    )
                                ) {
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = "\u201C",
                                            style = MaterialTheme.typography.displayLarge.copy(
                                                fontSize = 80.sp,
                                                fontFamily = FontFamily.Serif
                                            ),
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                            modifier = Modifier
                                                .padding(top = 8.dp, start = 16.dp)
                                        )

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    top = 40.dp,
                                                    start = 32.dp,
                                                    end = 32.dp,
                                                    bottom = 32.dp
                                                )
                                        ) {
                                            Text(
                                                text = content.verse,
                                                style = MaterialTheme.typography.headlineSmall.copy(
                                                    fontWeight = FontWeight.Light,
                                                    lineHeight = 32.sp,
                                                    fontSize = 20.sp
                                                ),
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(bottom = 24.dp)
                                            )

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = content.reference,
                                                    style = MaterialTheme.typography.labelLarge.copy(
                                                        fontWeight = FontWeight.SemiBold,
                                                        letterSpacing = 1.2.sp
                                                    ),
                                                    color = MaterialTheme.colorScheme.primary
                                                )

                                                Box(
                                                    modifier = Modifier
                                                        .width(48.dp)
                                                        .height(2.dp)
                                                        .background(
                                                            brush = Brush.horizontalGradient(
                                                                colors = listOf(
                                                                    MaterialTheme.colorScheme.primary,
                                                                    MaterialTheme.colorScheme.tertiary
                                                                )
                                                            ),
                                                            shape = RoundedCornerShape(1.dp)
                                                        )
                                                )
                                            }
                                        }
                                    }
                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 40.dp, start = 24.dp, end = 24.dp)
                                ) {
                                    if (content.thoughtText.isNotEmpty()) {
                                        MagazineSection(
                                            sectionNumber = "01",
                                            title = content.thoughtTitle.ifEmpty { "Reflection" },
                                            accentColor = MaterialTheme.colorScheme.tertiary
                                        ) {
                                            Text(
                                                text = content.thoughtText,
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    fontWeight = FontWeight.Light,
                                                    lineHeight = 28.sp,
                                                    fontSize = 16.sp
                                                ),
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(vertical = 12.dp)
                                            )
                                        }
                                    }

                                    // Prayer Section (02)
                                    if (content.prayerText.isNotEmpty()) {

                                        Spacer(modifier = Modifier.height(48.dp))
                                        MagazineSection(
                                            sectionNumber = "02",
                                            title = content.prayerTitle.ifEmpty { "Prayer" },
                                            accentColor = MaterialTheme.colorScheme.primary
                                        ) {
                                            Card(
                                                modifier = Modifier.fillMaxWidth()
                                                    .padding(vertical = 12.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                                                        alpha = 0.3f
                                                    )
                                                ),
                                                shape = RoundedCornerShape(24.dp)
                                            ) {
                                                Box(modifier = Modifier.fillMaxWidth()) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(80.dp)
                                                            .offset(
                                                                x = 150.dp,
                                                                y = (-40).dp
                                                            )
                                                            .background(
                                                                MaterialTheme.colorScheme.primary.copy(
                                                                    alpha = 0.08f
                                                                ),
                                                                CircleShape
                                                            )
                                                    )

                                                    Box(
                                                        modifier = Modifier
                                                            .size(60.dp)
                                                            .offset(
                                                                x = (-30).dp,
                                                                y = 60.dp
                                                            )
                                                            .background(
                                                                MaterialTheme.colorScheme.tertiary.copy(
                                                                    alpha = 0.08f
                                                                ),
                                                                CircleShape
                                                            )
                                                    )

                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(24.dp)
                                                    ) {
                                                        Row(
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            modifier = Modifier.padding(bottom = 16.dp) // Reduced spacing
                                                        ) {
                                                            Box(
                                                                modifier = Modifier
                                                                    .size(8.dp)
                                                                    .background(
                                                                        MaterialTheme.colorScheme.primary,
                                                                        CircleShape
                                                                    )
                                                            )
                                                            Spacer(modifier = Modifier.width(12.dp))
                                                            Text(
                                                                text = content.prayerTitle.ifEmpty { "A Prayer for Trust" },
                                                                style = MaterialTheme.typography.titleMedium.copy( // Reduced font size
                                                                    fontWeight = FontWeight.SemiBold
                                                                ),
                                                                color = MaterialTheme.colorScheme.primary
                                                            )
                                                        }

                                                        Text(
                                                            text = content.prayerText,
//                                                            baseStyle = MaterialTheme.typography.bodyLarge.copy(
//                                                                fontFamily = FontFamily.Serif,
//                                                                fontStyle = FontStyle.Italic,
//                                                                lineHeight = 26.sp, // Reduced line height
//                                                                fontSize = 16.sp // Reduced font size
//                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(48.dp))

                                    if (content.relatedScriptures.isNotEmpty()) {
                                        MagazineSection(
                                            sectionNumber = "03",
                                            title = "Explore Further",
                                            accentColor = MaterialTheme.colorScheme.secondary
                                        ) {
                                            Column(
                                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                                modifier = Modifier.padding(vertical = 12.dp)
                                            ) {
                                                content.relatedScriptures.chunked(2)
                                                    .forEach { pair ->
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.spacedBy(
                                                                12.dp
                                                            )
                                                        ) {
                                                            pair.forEach { scripture ->
                                                                ScriptureCard(
                                                                    scripture = scripture,
                                                                    modifier = Modifier.weight(1f),
                                                                )
                                                            }

                                                            if (pair.size == 1) {
                                                                Spacer(modifier = Modifier.weight(1f))
                                                            }
                                                        }
                                                    }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(120.dp))
                                }
                            }
                        }

                        // Bottom fade-out gradient overlay
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.background.copy(alpha = 0f),
                                            MaterialTheme.colorScheme.background.copy(alpha = 0.3f),
                                            MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                                            MaterialTheme.colorScheme.background
                                        ),
                                        startY = 0f,
                                        endY = Float.POSITIVE_INFINITY
                                    )
                                )
                        )

                        // Floating Action Bar
                        Card(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                            ),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 12.dp
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { /* Share action */ },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Icon(
                                        imageVector = Lucide.Share,
                                        contentDescription = "Share",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Share")
                                }

                                IconButton(
                                    onClick = { /* Bookmark action */ },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(
                                            MaterialTheme.colorScheme.surfaceVariant,
                                            RoundedCornerShape(16.dp)
                                        )
                                ) {
                                    Icon(
                                        imageVector = Lucide.Bookmark,
                                        contentDescription = "Bookmark",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (content.audioUrl.isNotEmpty() && !content.isLoading && content.error == null) {
                                    IconButton(
                                        onClick = { /* Like action */ },
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(
                                                MaterialTheme.colorScheme.surfaceVariant,
                                                RoundedCornerShape(16.dp)
                                            )
                                    ) {
                                        Icon(
                                            imageVector = if (isAudioPlaying) Lucide.Pause else Lucide.Play,
                                            contentDescription = if (isAudioPlaying) "Pause" else "Play",
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}