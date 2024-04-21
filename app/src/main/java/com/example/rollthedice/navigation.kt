package com.example.rollthedice


import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rollthedice.characters.CharacterDetailsView
import com.example.rollthedice.characters.CharacterListView
import com.example.rollthedice.characters.NewCharacterView
import com.example.rollthedice.dice.DiceView
import com.example.rollthedice.dice.RollHistoryView

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val routeName: String,
    val badgeAmount: Int? = null,
)

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController found!") }

@Composable
fun MainNavGraph() {
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
                        composable("list") { CharacterListView() }
                    }

                    navigation(startDestination = "roll", route = "dice") {
                        composable("roll") { DiceView() }
                    }
                }

                composable("history") { RollHistoryView() }
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

@Composable
fun BottomNavigationBar(navItems: List<TabBarItem>) {
    val nav = LocalNavController.current
    var selectedTabIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar {
        val navBackStackEntry by nav.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        navItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == tabBarItem.routeName } == true,
                onClick = {
                    selectedTabIndex = index
                    nav.navigate(tabBarItem.routeName) {
                        popUpTo(nav.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    TabBarIconView(
                        isSelected = selectedTabIndex == index,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title,
                        badgeAmount = tabBarItem.badgeAmount
                    )
                },
                label = { Text(tabBarItem.title) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    badgeAmount: Int? = null
) {
    BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
        Icon(
            imageVector = if (isSelected) {
                selectedIcon
            } else {
                unselectedIcon
            },
            contentDescription = title
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(count.toString())
        }
    }
}