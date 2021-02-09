package com.wanandroid.compose.ui.home

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.wanandroid.compose.ui.theme.WanandroidcomposeTheme

@Composable
fun Home(navBackStackEntry: NavBackStackEntry, navHostController: NavHostController) {
    WanandroidcomposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            Text(text = "主页")
        }
    }
}
