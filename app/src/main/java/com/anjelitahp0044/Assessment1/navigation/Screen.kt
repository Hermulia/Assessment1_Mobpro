package com.anjelitahp0044.Assessment1.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("MainScreen")
    data object About: Screen("AboutScreen")
}