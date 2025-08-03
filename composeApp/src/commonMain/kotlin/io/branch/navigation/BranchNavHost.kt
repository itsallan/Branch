package io.branch.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.branch.view.screens.home.HomeScreen
import io.branch.navigation.routes.HomeRoute

@Composable
fun BranchNavHost(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        composable<HomeRoute> {
           HomeScreen(navController)
        }
    }
}