package com.wanandroid.compose.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.wanandroid.compose.R
import com.wanandroid.compose.ui.Screen
import com.wanandroid.compose.vm.AccountAction
import com.wanandroid.compose.vm.LoginViewModel
import com.wanandroid.compose.vm.LoginViewState

@Composable
fun Fragment.AppDrawer(
    navigateTo: (Screen) -> Unit,
    closeDrawer: () -> Unit
) {
    val vm: LoginViewModel by remember { activityViewModels() }
    val logInfo by vm.viewState.observeAsState(LoginViewState())

    DisposableEffect(Unit, effect = {
        vm.dispatch(AccountAction.AutoLoginAction)
        onDispose { }
    })

    Column(modifier = Modifier.fillMaxWidth()) {
        Avatar(Modifier.clickable {
            navigateTo(Screen.Login)
        }, logInfo.loginInfo?.nickname)
    }
    Divider(color = MaterialTheme.colors.onSurface.copy(alpha = .2f))

    DrawerButton(
        icon = requireNotNull(Screen.Favorite.icon),
        label = stringResource(Screen.Favorite.resourceId),
        action = {
            navigateTo(Screen.Favorite)
            closeDrawer()
        }
    )

    DrawerButton(
        icon = Icons.Default.ExitToApp,
        label = "Logout",
        action = {
            vm.dispatch(AccountAction.LogoutAction)
        }
    )

}

@Composable
private fun Avatar(modifier: Modifier = Modifier, name: String? = null) {
    Box(
        modifier = modifier
            .height(Dp(200f))
            .fillMaxWidth()
    ) {
        val modifier = Modifier.fillMaxWidth()
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center)
        ) {
            Image(
                modifier = modifier,
                imageVector = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
                contentDescription = null, // decorative
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = name ?: "请登录",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }

}

@Composable
private fun DrawerButton(
    icon: ImageVector,
    label: String,
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colors
    val imageAlpha = 0.6f
    val textIconColor = colors.onSurface.copy(alpha = 0.6f)
    val backgroundColor = Color.Transparent

    val surfaceModifier = modifier
        .padding(start = 8.dp, top = 8.dp, end = 8.dp)
        .fillMaxWidth()
    Surface(
        modifier = surfaceModifier,
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        TextButton(
            onClick = action,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    imageVector = icon,
                    contentDescription = null, // decorative
                    colorFilter = ColorFilter.tint(textIconColor),
                    alpha = imageAlpha
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.body2,
                    color = textIconColor
                )
            }
        }
    }
}

@Preview("Drawer contents")
@Composable
fun PreviewAppDrawer() {
    Fragment().AppDrawer(
        navigateTo = { },
        closeDrawer = { }
    )
}
