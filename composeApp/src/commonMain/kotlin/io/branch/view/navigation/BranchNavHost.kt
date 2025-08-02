package io.branch.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.branch.view.HomeScreen
import io.branch.view.navigation.routes.HomeRoute

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