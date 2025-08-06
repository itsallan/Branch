package io.branch.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.branch.navigation.bottomnav.BottomNavigation
import io.branch.navigation.routes.BottomNavRoute
import io.branch.navigation.routes.HomeRoute

@Composable
fun BranchNavHost(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = BottomNavRoute
    ) {
        composable<BottomNavRoute> {
            BottomNavigation(navController)
        }

        composable<HomeRoute>{
            
        }
    }
}