package com.wanandroid.compose.ui.favorite

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.viewModels
import com.github.fragivity.navigator
import com.github.fragivity.pop
import com.wanandroid.compose.ComposeFragment
import com.wanandroid.compose.R
import com.wanandroid.compose.ui.article_list.ArticleItem
import com.wanandroid.compose.ui.article_list.CollectionItem
import com.wanandroid.compose.ui.theme.WanandroidcomposeTheme
import com.wanandroid.compose.utils.PagedList
import com.wanandroid.compose.vm.favorite.FavoriteListViewModel
import com.wanandroid.compose.vm.mvi_base.PagedListingAction
import com.wanandroid.compose.vm.mvi_base.PagedListingViewState
import com.wanandroid.compose.vm.reducer.CollectAction

@Composable
fun ComposeFragment.FavoriteScreen() {
    WanandroidcomposeTheme {
        // A surface container using the 'background' color from the theme
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
                                Text(stringResource(id = R.string.favorite))
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
                    FavoriteList()
                }
            }
        }
    }
}


@Composable
private fun ComposeFragment.FavoriteList(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState()
) {

    val vm: FavoriteListViewModel by remember { viewModels() }
    val viewState by vm.viewState.observeAsState(PagedListingViewState())

    DisposableEffect(Unit) {
        vm.dispatch(PagedListingAction.Refresh)
        onDispose {}
    }

    PagedList(
        modifier = modifier,
        datas = viewState.data,
        listState = listState,
        onLoadMore = remember(viewState.page) {
            {
                vm.dispatch(PagedListingAction.LoadMore)
            }
        }
    ) { _, item ->

        CollectionItem(item) {
            vm.dispatch(CollectAction(item.originId, false)) {
                vm.dispatch(PagedListingAction.Refresh)
            }
        }
    }

}
