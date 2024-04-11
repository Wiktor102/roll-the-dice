package com.example.rollthedice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rollthedice.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val homeTab = TabBarItem(
                title = "Postacie",
                selectedIcon = Icons.Filled.People,
                unselectedIcon = Icons.Outlined.People
            )

            val alertsTab = TabBarItem(
                title = "Kostka",
                selectedIcon = Icons.Filled.Casino,
                unselectedIcon = Icons.Outlined.Casino
            )

            val tabBarItems = listOf(homeTab, alertsTab)
            val navController = rememberNavController()

            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompositionLocalProvider(LocalNavController provides navController) {
                        Scaffold(
                            topBar = { TopAppBar(title = { Text("Roll The Dice") }) },
                            bottomBar = { BottomNavigationBar(navItems = tabBarItems) }
                        ) {
                            NavHost(
                                navController = navController,
                                startDestination = homeTab.title,
                                Modifier.padding(it)
                            ) {
                                composable(homeTab.title) {
                                    Text(homeTab.title)
                                }
                                composable(alertsTab.title) {
                                    Text(alertsTab.title)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}