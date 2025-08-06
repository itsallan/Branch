package io.branch.navigation.bottomnav

import com.composables.icons.lucide.Lucide
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.composables.icons.lucide.BookOpen
import com.composables.icons.lucide.CircleUserRound
import com.composables.icons.lucide.House
import io.branch.view.screens.verses.HomeScreen

@Composable
fun BottomNavigation(
    navController: NavController,
) {
    val containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
    val selectedColor = MaterialTheme.colorScheme.primary
    val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
    val dividerColor = MaterialTheme.colorScheme.outlineVariant

    var selectedTab by remember { mutableIntStateOf(0) }

    val navItems = listOf(
        NavItem("Today", Lucide.House),
        NavItem("Verses", Lucide.BookOpen),
        NavItem("Profile", Lucide.CircleUserRound)
    )

    Scaffold(
        bottomBar = {
            Column {
                HorizontalDivider(color = dividerColor, modifier = Modifier.height(1.dp))
                NavigationBar(
                    containerColor = containerColor,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    tonalElevation = 0.dp
                ) {
                    navItems.forEachIndexed { index, item ->
                        val isSelected = selectedTab == index
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(24.dp),
                                    tint = if (isSelected) selectedColor else unselectedColor
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    color = if (isSelected) selectedColor else unselectedColor,
                                    style = MaterialTheme.typography.labelSmall,
                                    textAlign = TextAlign.Center
                                )
                            },
                            selected = isSelected,
                            onClick = { selectedTab = index },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = selectedColor,
                                unselectedIconColor = unselectedColor,
                                selectedTextColor = selectedColor,
                                unselectedTextColor = unselectedColor,
                                indicatorColor = containerColor
                            )
                        )
                    }
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                0 -> {
                   // TodayScreen(navController)
                }

                1 -> {
                    HomeScreen(navController)
                   // DevotionScreen(navController)
                }

                2 -> {
                   // WorshipApp()
                }
            }
        }
    }
}