package com.wanandroid.compose

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.github.fragivity.applySlideInOut
import com.github.fragivity.navigator
import com.github.fragivity.push
import com.github.fragivity.swipeback.swipeBackLayout

class ComposeFragment(
    private val swipeBack: Boolean = false,
    private val content: @Composable ComposeFragment.() -> Unit
    ) : Fragment(R.layout.fragment_compose) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.compose_view).setContent {
            this@ComposeFragment.content()
        }
        swipeBackLayout.setEnableGesture(swipeBack)
    }


    fun openNewTab(content: @Composable ComposeFragment.() -> Unit) {
        navigator.push(
            optionsBuilder = { applySlideInOut() },
            block = { ComposeFragment(true, content) }
        )
    }
}
