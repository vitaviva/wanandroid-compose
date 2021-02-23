package com.wanandroid.compose.ui

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wanandroid.compose.R
import com.wanandroid.compose.utils.getMutableStateOf


private const val SIS_SCREEN = "sis_screen"
private const val SIS_ROUTE = "screen_name"


sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Main : Screen("wanandroid/main", -1, Icons.Default.Home)
    object Home : Screen("wanandroid/main/home", R.string.home, Icons.Default.Home)
    object Channel : Screen("wanandroid/main/channel", R.string.Channel, Icons.Default.Menu)
    object Settings : Screen("wanandroid/settings", R.string.Channel, Icons.Default.Settings)
    object Favorite : Screen("wanandroid/favorite", R.string.favorite, Icons.Default.Favorite)

    init {
        _map[route] = this
    }

    companion object {
        private val _map: MutableMap<String, Screen> = mutableMapOf()

        fun of(route: String) = _map[route] ?: Home
    }
}

class ScreenViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    var currentScreen: Screen by savedStateHandle.getMutableStateOf<Screen>(
        key = SIS_SCREEN,
        default = Screen.Main,
        save = { it.toBundle() },
        restore = { it.toScreen() }
    )

    @MainThread
    fun onBack(): Boolean {
        val wasHandled = currentScreen != Screen.Main
        currentScreen = Screen.Main
        return wasHandled
    }

    @MainThread
    fun navigateTo(screen: Screen) {
        currentScreen = screen
    }
}


/**
 * Convert a screen to a bundle that can be stored in [SavedStateHandle]
 */
internal fun Screen.toBundle(): Bundle {
    return bundleOf(SIS_ROUTE to route)
}

/**
 * Read a bundle stored by [Screen.toBundle] and return desired screen.
 *
 * @return the parsed [Screen]
 * @throws IllegalArgumentException if the bundle could not be parsed
 */
private fun Bundle.toScreen(): Screen {

    val route = getString(SIS_ROUTE)
    return when (route) {
        Screen.Home.route -> Screen.Home
        Screen.Favorite.route -> Screen.Favorite
        else -> error("")
    }
}
