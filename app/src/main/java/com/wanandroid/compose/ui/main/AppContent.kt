package com.wanandroid.compose.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.wanandroid.compose.ComposeFragment
import com.wanandroid.compose.ui.Screen
import com.wanandroid.compose.ui.channel.ChannelScreen
import com.wanandroid.compose.ui.favorite.FavoriteScreen
import com.wanandroid.compose.ui.home.HomeScreen
import com.wanandroid.compose.ui.login.LoginScreen
import com.wanandroid.compose.ui.search.SearchScreen
import com.wanandroid.compose.ui.theme.WanandroidcomposeTheme
import kotlinx.coroutines.launch
import mockFragment


@Composable
fun ComposeFragment.AppContent() {
    WanandroidcomposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {

            val coroutineScope = rememberCoroutineScope()
            Box {

                val scaffoldState = rememberScaffoldState()
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute =
                    navBackStackEntry?.run { destination.arguments[KEY_ROUTE]?.defaultValue } as? String
                        ?: ""

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(id = Screen.of(currentRoute).resourceId)) },
                            navigationIcon = {
                                IconButton(
                                    onClick = { coroutineScope.launch { scaffoldState.drawerState.open() } }
                                ) {
                                    Icon(Icons.Default.Menu, "")
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        openNewTab { SearchScreen() }
                                    }
                                ) {
                                    Icon(Icons.Default.Search, "")
                                }
                            }
                        )
                    },
                    scaffoldState = scaffoldState,
                    drawerContent = {
                        AppDrawer(
                            navigateTo = {
                                when (it) {
                                    Screen.Favorite -> openNewTab { FavoriteScreen() }
                                    Screen.Login -> openNewTab { LoginScreen() }
                                }
                            },
                            closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } })
                    },
                    bottomBar = {
                        BottomNavigation(currentRoute, navController)
                    }
                ) {

                    val modifier = remember { Modifier.padding(it) }
                    NavHost(navController, startDestination = Screen.Home.route) {
                        composable(Screen.Home.route) {
                            HomeScreen(modifier)
                        }
                        composable(Screen.Channel.route) {
                            ChannelScreen(modifier)
                        }
                    }

                }

            }

        }

    }
}


@Preview
@Composable
fun previewAppContent() {

    mockFragment().AppContent()

}

@Composable
fun BottomNavigation(currentRoute: String, navController: NavController) {
    val items = remember { listOf(Screen.Home, Screen.Channel) }

    BottomNavigation(
        modifier = Modifier.fillMaxWidth(),
        contentColor = Color.White,
    ) {
        items.forEach { screen ->
            BottomNavigationItem(
                label = { Text(text = stringResource(id = screen.resourceId)) },
                icon = { Icon(requireNotNull(screen.icon), null) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo = navController.graph.startDestination
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                    }
                },
                // alwaysShowLabels is used to set if you want to show the labels always or
                // just for the current item.
                alwaysShowLabel = true
            )
        }
    }
}