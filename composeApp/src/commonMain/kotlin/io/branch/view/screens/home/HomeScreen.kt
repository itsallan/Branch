package io.branch.view.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import io.branch.data.repository.ScriptureRepository
import io.branch.utils.getTodayIndex
import io.branch.view.components.header.AppHeader
import io.branch.view.components.shared.HomeIndicator
import io.branch.view.components.content.ScriptureContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {
    val scriptures = ScriptureRepository.getWeeklyScriptures()
    var selectedDay by remember { mutableStateOf(getTodayIndex(scriptures)) }
    var bookmarkedDays by remember { mutableStateOf(setOf<Int>()) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AppHeader(
                scriptures = scriptures,
                selectedDayIndex = selectedDay,
                onDaySelected = { selectedDay = it },
            )
            ScriptureContent(
                scriptures = scriptures,
                selectedDayIndex = selectedDay,
                bookmarkedDays = bookmarkedDays,
                onBookmarkToggle = { index ->
                    bookmarkedDays = if (bookmarkedDays.contains(index)) {
                        bookmarkedDays - index
                    } else {
                        bookmarkedDays + index
                    }
                },
                onShare = { index ->

                },
                modifier = Modifier.weight(1f)
            )

            HomeIndicator()
        }
    }
}
