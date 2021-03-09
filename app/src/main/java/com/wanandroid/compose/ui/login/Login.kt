package com.wanandroid.compose.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.fragivity.navigator
import com.github.fragivity.pop
import com.wanandroid.compose.R
import com.wanandroid.compose.ui.theme.WanandroidcomposeTheme
import com.wanandroid.compose.vm.LoginAction
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
                Column {
                    TextField(label = {
                        Text("Name")
                    }, value = name, onValueChange = { name = it })
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(
                        label = {
                            Text("Password")
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                        value = pwd,
                        onValueChange = { pwd = it })

                    Spacer(modifier = Modifier.height(50.dp))

                    Button(onClick = {
                        vm.dispatch(LoginAction(name, pwd))
                    }) {
                        Text("Login")
                    }
                }


            }
        }
    }
}
