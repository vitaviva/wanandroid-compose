package com.wanandroid.compose.ui.detail

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import com.github.fragivity.navigator
import com.github.fragivity.pop
import com.wanandroid.compose.ui.theme.WanandroidcomposeTheme
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import java.lang.Exception


@Composable
fun Fragment.DetailScreen(url: String) {
    WanandroidcomposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxSize()
        ) {

            var title by remember { mutableStateOf("") }
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Row(
                                modifier = Modifier.horizontalScroll(
                                    state = rememberScrollState(),
                                )
                            ) {
                                Text(title, maxLines = 1)
                            }

                        },
                        navigationIcon = {
                            IconButton(
                                onClick = { navigator.pop() }
                            ) {
                                Icon(Icons.Filled.ArrowBack, null)
                            }
                        }
                    )
                },
            ) {
                AndroidView(factory = {
                    WebView(requireContext()).apply {
                        settings.javaScriptEnabled = true
                        webChromeClient = object : WebChromeClient() {
                            override fun onReceivedTitle(view: WebView?, str: String?) {
                                super.onReceivedTitle(view, title)
                                str?.let { title = it }
                            }
                        }
                        webViewClient = object : WebViewClient() {

//                            override fun shouldOverrideUrlLoading(
//                                view: WebView,
//                                request: WebResourceRequest?
//                            ): Boolean {
//                                return try {
//                                    if (url.startsWith("http:") || url.startsWith("https:")) {
//                                        view.loadUrl(url)
//                                    } else {
//                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                                        requireActivity().startActivity(intent)
//                                    }
//                                    true
//                                } catch (e: Exception) {
//                                    false
//                                }
//                            }
                        }
                    }
                }) { webView ->
                    webView.loadUrl(url)
                }
            }
        }
    }
}
