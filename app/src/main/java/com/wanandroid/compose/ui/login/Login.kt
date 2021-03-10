package com.wanandroid.compose.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.fragivity.navigator
import com.github.fragivity.pop
import com.wanandroid.compose.R
import com.wanandroid.compose.ui.theme.WanandroidcomposeTheme
import com.wanandroid.compose.vm.AccountAction
import com.wanandroid.compose.vm.LoginViewModel

@Composable
fun Fragment.LoginScreen() {
    WanandroidcomposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxSize()
        ) {

            val vm: LoginViewModel by remember { activityViewModels() }

            val loginInfo by vm.viewState.observeAsState()

            if (loginInfo?.loginInfo != null) {
                navigator.pop()
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(stringResource(id = R.string.login), maxLines = 1)
                        },
                        navigationIcon = {
                            IconButton(onClick = { navigator.pop() }
                            ) {
                                Icon(Icons.Filled.ArrowBack, null)
                            }
                        }
                    )
                },
            ) {
                var name by remember { mutableStateOf("") }
                var pwd by remember { mutableStateOf("") }

                val passwordVisualTransformation by remember {
                    mutableStateOf<VisualTransformation>(
                        PasswordVisualTransformation()
                    )
                }

                Box(Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.align(Alignment.TopCenter)) {
                        TextField(label = {
                            Text("Name")
                        }, value = name, onValueChange = { name = it })
                        Spacer(modifier = Modifier.height(20.dp))
                        TextField(
                            label = {
                                Text("Password")
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            visualTransformation = passwordVisualTransformation,
                            value = pwd,
                            onValueChange = { pwd = it })

                        Spacer(modifier = Modifier.height(50.dp))

                        Button(onClick = {
                            vm.dispatch(AccountAction.LoginAction(name, pwd))
                        }) {
                            Text("Login")
                        }
                    }

                }


            }
        }
    }
}
