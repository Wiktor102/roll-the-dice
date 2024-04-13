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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rollthedice.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val charactersTab = TabBarItem(
                title = "Postacie",
                routeName = "characters",
                selectedIcon = Icons.Filled.People,
                unselectedIcon = Icons.Outlined.People
            )

            val diceTab = TabBarItem(
                title = "Kostka",
                routeName = "dice",
                selectedIcon = Icons.Filled.Casino,
                unselectedIcon = Icons.Outlined.Casino,
            )

            val tabBarItems = listOf(charactersTab, diceTab)
            val navController = rememberNavController()
            val charactersViewModel: CharactersViewModel = viewModel()

            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompositionLocalProvider(LocalNavController provides navController) {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        val showBn = currentDestination?.hierarchy?.any {
                            listOf(
                                charactersTab.routeName,
                                diceTab.routeName
                            ).contains(it.route)
                        } == true;
                        Scaffold(
                            bottomBar = { if (showBn) BottomNavigationBar(navItems = tabBarItems) }
                        ) {
                            NavHost(
                                navController = navController,
                                startDestination = "main",
                                Modifier.padding(it)
                            ) {
                                navigation(startDestination = "characters", route = "main") {
                                    navigation(startDestination = "list", route = "characters") {
                                        composable("list") { CharactersListView() }
                                    }

                                    composable(diceTab.routeName) {
                                        Text(diceTab.title)
                                    }
                                }
                                composable("${charactersTab.routeName}/create") { NewCharacterView() }
                                composable(
                                    "character/{characterName}",
                                    arguments = listOf(navArgument("characterName") {
                                        type = NavType.StringType
                                    })
                                ) { backStackEntry ->
                                    backStackEntry.arguments?.getString("characterName")
                                        ?.let { it1 -> CharacterDetailsView(it1) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}