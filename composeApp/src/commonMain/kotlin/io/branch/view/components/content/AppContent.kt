package io.branch.view.components.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.branch.data.model.ScriptureData


@Composable
fun ScriptureContent(
    scriptures: List<ScriptureData>,
    selectedDayIndex: Int,
    bookmarkedDays: Set<Int>,
    onBookmarkToggle: (Int) -> Unit,
    onShare: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(topStart = 44.dp, topEnd = 44.dp)
                )
                .padding(top = 34.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 32.dp,
                top = 24.dp,
                bottom = 24.dp
            )
        ) {
            itemsIndexed(scriptures) { index, scripture ->
                ScriptureDaySection(
                    scripture = scripture,
                    isBookmarked = bookmarkedDays.contains(index),
                    onBookmarkClick = { onBookmarkToggle(index) },
                    onShareClick = { onShare(index) }
                )

                if (index < scriptures.size - 1) {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .offset(y = 34.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant,
                            Color.Transparent
                        )
                    )
                )
                .align(Alignment.TopCenter)
        )

//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(40.dp)
//                .background(
//                    brush = Brush.verticalGradient(
//                        colors = listOf(
//                            Color.Transparent,
//                            MaterialTheme.colorScheme.surfaceVariant
//                        )
//                    )
//                )
//                .align(Alignment.BottomCenter)
//        )
    }
}