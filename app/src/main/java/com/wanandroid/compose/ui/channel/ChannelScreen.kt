package com.wanandroid.compose.ui.channel

import ContentLoadingLayout
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.wanandroid.compose.components.FullScreenLoading
import com.wanandroid.compose.data.bean.ChildrenBean
import com.wanandroid.compose.ui.article_list.ArticleItem
import com.wanandroid.compose.utils.PagedList
import com.wanandroid.compose.vm.ChannelAction
import com.wanandroid.compose.vm.ChannelListViewModel
import com.wanandroid.compose.vm.ChannelTabViewModel
import com.wanandroid.compose.vm.ChannelViewState
import com.wanandroid.compose.vm.mvi_base.PagedListingAction
import com.wanandroid.compose.vm.mvi_base.PagedListingViewState

@Composable
fun Fragment.ChannelScreen(
    modifier: Modifier = Modifier,
    navBackStackEntry: NavBackStackEntry,
    navHostController: NavHostController
) {


    val vm: ChannelTabViewModel by remember { viewModels() }
    val viewState by vm.viewState.observeAsState(ChannelViewState())

    DisposableEffect(Unit) {
        vm.dispatch(ChannelAction.LoadAllChannels)
        onDispose { }
    }

    if (viewState.curChannel == null) return

    Column {
        Row {
            ChannelTabs(
                categories = viewState.allChannels,
                selectedCategory = viewState.curChannel,
                onCategorySelected = { vm.dispatch(ChannelAction.SwitchChannel(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Icon(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .align(Alignment.CenterVertically),
                imageVector = Icons.Default.Menu,
                contentDescription = ""
            )

        }

        key(viewState.curChannel!!.id) {
            ChannelList(cid = viewState.curChannel!!.id)
        }

    }

}


@Composable
private fun ChannelTabs(
    categories: List<ChildrenBean> = emptyList(),
    selectedCategory: ChildrenBean?,
    onCategorySelected: (ChildrenBean) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = categories.indexOfFirst { it == selectedCategory }
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        divider = {}, /* Disable the built-in divider */
        edgePadding = 24.dp,
        modifier = modifier
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onCategorySelected(category) }
            ) {
                Text(
                    text = category.name,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}


@Composable
fun Fragment.ChannelList(
    modifier: Modifier = Modifier,
    cid: Int
) {
    val vm: ChannelListViewModel = viewModel(cid.toString(), remember {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChannelListViewModel(cid) as T
            }
        }
    })

    val viewState by vm.viewState.observeAsState(PagedListingViewState())
    val listState = rememberLazyListState()

    val onRefresh = remember {
        {
            vm.dispatch(PagedListingAction.Refresh) {
                listState.snapToItemIndex(0)
            }
        }
    }

    DisposableEffect(Unit) {
        onRefresh.invoke()
        onDispose {}
    }

    ContentLoadingLayout(
        empty = viewState.initialLoad,
        emptyContent = { FullScreenLoading() },
        loading = viewState.loading,
        onRefresh = onRefresh,
        content = {
            if (viewState.hasError) {
                TextButton(onClick = onRefresh, modifier.fillMaxSize()) {
                    Text("Tap to load content", textAlign = TextAlign.Center)
                }
            } else {
                Box(modifier.fillMaxSize()) {
                    val context = LocalContext.current
                    PagedList(
                        modifier = modifier,
                        datas = viewState.data,
                        listState = listState,
                        onLoadMore = remember(viewState.page) {
                            {
                                vm.dispatch(PagedListingAction.LoadMore)
                                Toast.makeText(
                                    context,
                                    "loadMore: ${viewState.page}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    ) { _, item ->
                        ArticleItem(item)
                    }
                }
            }
        }
    )
}