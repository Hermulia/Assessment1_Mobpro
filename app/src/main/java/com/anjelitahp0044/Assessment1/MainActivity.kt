package com.anjelitahp0044.Assessment1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.rememberNavController
import com.anjelitahp0044.Assessment1.navigation.AppNavigation
import com.anjelitahp0044.Assessment1.ui.theme.Assessment1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val isDarkTheme = rememberSaveable { mutableStateOf(false) }

            Assessment1(darkTheme = isDarkTheme.value) {
                AppNavigation(navController, isDarkTheme)
            }

        }
    }
}
