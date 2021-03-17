package com.wanandroid.compose.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.github.fragivity.navigator
import com.github.fragivity.pop
import com.wanandroid.compose.ui.theme.WanandroidcomposeTheme
import mockFragment


@Composable
fun Fragment.SearchScreen() {
    WanandroidcomposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxSize()
        ) {

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Box(
                                modifier = Modifier
                                    .padding(top = 5.dp, bottom = 5.dp)
                            ) {
                                TextField(
                                    value = "",
                                    onValueChange = { },
                                    placeholder = {
                                        Text("多个关键词空格")
                                    },
                                )
                            }

                        },
                        navigationIcon = {
                            IconButton(onClick = { navigator.pop() }
                            ) {
                                Icon(Icons.Filled.ArrowBack, null)
                            }
                        },
                        actions = {
                            IconButton(onClick = { }
                            ) {
                                Icon(Icons.Filled.Clear, null)
                            }
                        }
                    )
                },
            ) {


            }
        }
    }
}


@Preview
@Composable
fun previewSearch() {
    mockFragment().SearchScreen()
}