package com.wanandroid.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.github.fragivity.applyFadeInOut
import com.github.fragivity.applySlideInOut
import com.github.fragivity.navigator
import com.github.fragivity.push
import com.github.fragivity.swipeback.swipeBackLayout

class ComposeFragment(
    private val swipeBack: Boolean = false,
    private val content: @Composable ComposeFragment.() -> Unit
    ) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_compose, container, false
        ).apply {
            findViewById<ComposeView>(R.id.compose_view).setContent {
                this@ComposeFragment.content()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeBackLayout.setEnableGesture(swipeBack)
    }


    fun openNewTab(content: @Composable ComposeFragment.() -> Unit) {
        navigator.push(
            optionsBuilder = { applyFadeInOut() },
            block = { ComposeFragment(true, content) }
        )
    }
}
