package com.anjelitahp0044.Assessment1.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anjelitahp0044.Assessment1.screens.AboutScreen
import com.anjelitahp0044.Assessment1.screens.MainScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    isDarkTheme: MutableState<Boolean>
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            MainScreen(navController = navController, isDarkTheme = isDarkTheme)
        }
        composable(route = Screen.About.route) {
            AboutScreen(navController = navController, isDarkTheme = isDarkTheme)
        }
    }
}

