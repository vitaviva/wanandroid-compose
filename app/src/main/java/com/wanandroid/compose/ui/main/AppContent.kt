package com.wanandroid.compose.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.wanandroid.compose.ui.Screen
import com.wanandroid.compose.ui.home.Home
import com.wanandroid.compose.ui.knowledge.Knowledge
import com.wanandroid.compose.ui.theme.WanandroidcomposeTheme


@Composable
fun AppContent(navigateTo: (Screen) -> Unit) {
    WanandroidcomposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {

            val scaffoldState = rememberScaffoldState()
            val navController = rememberNavController()

            Box {
                Scaffold(
                    scaffoldState = scaffoldState,
                    drawerContent = {
                        AppDrawer(
                            navigateTo = { navigateTo(it) },
                            closeDrawer = { scaffoldState.drawerState.close() })
                    },
                    bottomBar = {
                        BottomNavigation(navController)
                    }
                ) {

                    NavHost(navController, startDestination = Screen.Home.route) {
                        composable(Screen.Home.route) { Home(it, navController) }
                        composable(Screen.Knowledge.route) { Knowledge(it, navController) }
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
fun BottomNavigation(navController: NavController) {
    val items = remember { listOf(Screen.Home, Screen.Knowledge) }

    BottomNavigation(
        modifier = Modifier
//            .padding(16.dp)
            .fillMaxWidth(),
//        backgroundColor = Color.Black,
        contentColor = Color.White,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.run { destination.arguments[KEY_ROUTE]?.defaultValue }
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