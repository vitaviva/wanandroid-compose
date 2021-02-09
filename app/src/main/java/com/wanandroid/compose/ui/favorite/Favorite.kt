package com.wanandroid.compose.ui.favorite

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.wanandroid.compose.ui.theme.WanandroidcomposeTheme

@Composable
fun Favorite() {
    WanandroidcomposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxSize()) {
            Text(text = "收藏")
        }
    }
}
