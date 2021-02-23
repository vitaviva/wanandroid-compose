package com.wanandroid.compose.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.wanandroid.compose.ComposeFragment
import com.wanandroid.compose.ui.Screen
import com.wanandroid.compose.ui.favorite.Favorite
import com.wanandroid.compose.ui.home.HomeScreen
import com.wanandroid.compose.ui.channel.ChannelScreen
import com.wanandroid.compose.ui.theme.WanandroidcomposeTheme


@Composable
fun ComposeFragment.AppContent() {
    WanandroidcomposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {

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
                                    onClick = { scaffoldState.drawerState.open() }
                                ) {
                                    Icon(Icons.Filled.Menu, "")
                                }
                            }
                        )
                    },
                    scaffoldState = scaffoldState,
                    drawerContent = {
                        AppDrawer(
                            navigateTo = {
                                when (it) {
                                    is Screen.Favorite -> openNewTab { Favorite() }
                                    else -> error("")
                                }
                            },
                            closeDrawer = { scaffoldState.drawerState.close() })
                    },
                    bottomBar = {
                        BottomNavigation(currentRoute, navController)
                    }
                ) {

                    val modifier = remember { Modifier.padding(it) }
                    NavHost(navController, startDestination = Screen.Home.route) {
                        composable(Screen.Home.route) {
                            HomeScreen(modifier, it, navController)
                        }
                        composable(Screen.Channel.route) {
                            ChannelScreen(modifier, it, navController)
                        }
                    }

                }

//                val currentScreen = screenViewModel.currentScreen
//                val visible = remember(currentScreen) { currentScreen != Screen.Main }
//                AnimatedVisibility(
//                    visible = visible,
//                    enter = slideIn(
//                        // Specifies the starting offset of the slide-in to be 1/4 of the width to the right,
//                        // 100 (pixels) below the content position, which results in a simultaneous slide up
//                        // and slide left.
//                        { fullSize -> IntOffset(fullSize.width / 4, 100) },
//                        tween(500, easing = LinearOutSlowInEasing)
//                    ),
//                    exit = slideOut(
//                        // The offset can be entirely independent of the size of the content. This specifies
//                        // a target offset 180 pixels to the left of the content, and 50 pixels below. This will
//                        // produce a slide-left combined with a slide-down.
//                        { IntOffset(-180, 50) },
//                        tween(500, easing = FastOutSlowInEasing)
//                    )
//                ) {
//                    // Content that needs to appear/disappear goes here:
////                            Box(Modifier.fillMaxWidth().height(200.dp)) {}
//                    val pre = remember() {
//                        currentScreen
//                    }
////                            when (currentScreen) {
////                                is Screen.Favorite -> Favorite()
////                                is Screen.Main -> {
////                                }
////                                else -> error("")
////                            }
//                }

//                        Crossfade(current = screenViewModel.currentScreen) {
//                            when (it) {
//                                is Screen.Favorite -> Favorite()
//                                is Screen.Main -> {}
//                                else -> error("")
//                            }
//                        }
            }

        }
    }
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
                icon = { Icon(screen.icon, null) },
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
                alwaysShowLabels = true
            )
        }
    }
}