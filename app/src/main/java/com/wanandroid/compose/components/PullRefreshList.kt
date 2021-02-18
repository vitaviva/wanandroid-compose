package com.wanandroid.compose.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


@Composable
inline fun <T> PullRefreshList(
    modifier: Modifier = Modifier,
    list: List<T>,
    refreshState: State<Boolean> = remember { mutableStateOf(false) },
    listState: LazyListState = rememberLazyListState(),
    noinline onRefresh: () -> Unit = {},
    noinline  onLoadMore: () -> Unit = {},
    loadMoreOffset: Int = 2,
    crossinline itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {
    val context = AmbientContext.current
    val refreshLayout = remember { SwipeRefreshLayout(context) }

    AndroidView(
        modifier = modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
        viewBlock = { context ->
            // Creates custom view
            refreshLayout.apply {

                setOnRefreshListener {
                    onRefresh()
//                    vm.refresh()
                }

                addView(
                    ComposeView(context = context).apply {
                        setContent {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                itemsIndexed(list) { index, item ->
                                    itemContent(index, item)
//                                    Text(
//                                        modifier = Modifier.height(Dp(100f)),
//                                        text = "${item.title}"
//                                    )

                                    DisposableEffect(list.size) {
                                        if (list.size - index < loadMoreOffset) {
                                            onLoadMore()
//                                            vm.loadMore()
//                                            Toast.makeText(
//                                                context,
//                                                "loadMore",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
                                        }
                                        onDispose { }
                                    }

                                }
                            }
                        }
                    }

                )
            }
        },
        update = { view ->
            // View's been inflated or state read in this block has been updated
            // Add logic here if necessary

            // As selectedItem is read here, AndroidView will recompose
            // whenever the state changes
            // Example of Compose -> View communication

            refreshLayout.isRefreshing = refreshState.value


            if (listState.firstVisibleItemIndex == 0) {
                view.isRefreshing = false
                view.isEnabled = true
            } else {
                view.isEnabled = false
            }
        }
    )

    AndroidView(
        modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
        viewBlock = { context ->
            // Creates custom view
            refreshLayout.apply {

                setOnRefreshListener {
                    onRefresh()
                }

                addView(
                    ComposeView(context = context).apply {
                        setContent {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                itemsIndexed(list) { index, item ->
                                    itemContent(index, item)

                                    DisposableEffect(list.size) {
                                        if (list.size - index < 2) {
                                            onLoadMore()
                                        }
                                        onDispose { }
                                    }

                                }
                            }
                        }
                    }

                )
            }
        },
        update = { view ->
            // View's been inflated or state read in this block has been updated
            // Add logic here if necessary

            // As selectedItem is read here, AndroidView will recompose
            // whenever the state changes
            // Example of Compose -> View communication

            view.isRefreshing = refreshState.value

            if (listState.firstVisibleItemIndex == 0) {
                view.isRefreshing = false
                view.isEnabled = true
            } else {
                view.isEnabled = false
            }
        }
    )

}