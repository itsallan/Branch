package io.branch.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.branch.navigation.bottomnav.BottomNavigation
import io.branch.navigation.routes.BottomNavRoute
import io.branch.navigation.routes.DevotionalDetailRoute
import io.branch.navigation.routes.HomeRoute
import io.branch.view.screens.details.DetailsScreen
import io.branch.view.screens.home.HomeScreen
import io.branch.view.screens.verses.VerseScreen

@Composable
fun BranchNavHost(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = BottomNavRoute
    ) {
        composable<BottomNavRoute> {
            VerseScreen(navController)
        }

        composable<HomeRoute>{
            HomeScreen(navController)
        }

        composable<DevotionalDetailRoute>{
            val details = it.toRoute<DevotionalDetailRoute>()
            DetailsScreen(data = details, navController)
        }
    }
}