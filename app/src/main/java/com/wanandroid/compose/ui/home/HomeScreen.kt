package com.wanandroid.compose.ui.home

import ContentLoadingLayout
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.github.vitaviva.mvi.PagedListingAction
import com.github.vitaviva.mvi.PagedListingViewState
import com.wanandroid.compose.ComposeFragment
import com.wanandroid.compose.components.FullScreenLoading
import com.wanandroid.compose.components.Pager
import com.wanandroid.compose.components.PagerState
import com.wanandroid.compose.data.bean.ArticleBean
import com.wanandroid.compose.data.bean.BannerBean
import com.wanandroid.compose.ui.article_list.ArticleItem
import com.wanandroid.compose.ui.detail.DetailScreen
import com.wanandroid.compose.utils.Fab
import com.wanandroid.compose.utils.PagedList
import com.wanandroid.compose.vm.HomeListViewModel
import com.wanandroid.compose.vm.reducer.CollectAction
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun ComposeFragment.HomeScreen(
    modifier: Modifier = Modifier
) {

    val vm: HomeListViewModel by remember { viewModels() }
    val viewState by vm.viewState.observeAsState(PagedListingViewState())
    val listState = rememberLazyListState()
    val pagerState = remember { PagerState() }

    val onRefresh = remember {
        {
            vm.dispatch(PagedListingAction.Refresh) {
                listState.scrollToItem(0)
                pagerState.currentPage = 0
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

                    HomeList(
                        vm = vm,
                        pagerState = pagerState,
                        listState = listState
                    )

                    Fab(action = onRefresh)
                }
            }
        }
    )

}


@Composable
private fun ComposeFragment.HomeList(
    modifier: Modifier = Modifier,
    vm: HomeListViewModel,
    pagerState: PagerState = remember { PagerState() },
    listState: LazyListState = rememberLazyListState()
) {

    val viewState by vm.viewState.observeAsState(PagedListingViewState())
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

        when (item) {
            is List<*> ->
                @Suppress("UNCHECKED_CAST")
                BannerItem(pagerState, item as List<BannerBean>)
            is ArticleBean ->
                ArticleItem(item) {
                    vm.dispatch(CollectAction(item.id, it))
                }

            else -> error("")
        }

    }

}

@Composable
private fun ComposeFragment.BannerItem(pagerState: PagerState, list: List<BannerBean>) {

    pagerState.maxPage = (list.size - 1).coerceAtLeast(0)

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(Modifier.height(16.dp))

        Pager(
            state = pagerState,
            modifier = Modifier
                .padding(start = 10.dp, top = 16.dp, end = 10.dp)
                .fillMaxWidth()
                .height(200.dp)
        ) {
            val banner = list[page]

            CarouselItem(
                podcastImageUrl = banner.imagePath,
                title = banner.title,
                onUnfollowedClick = { /*onPodcastUnfollowed(podcast.uri)*/ },
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxHeight()
                    .clickable {
                        openNewTab {
                            DetailScreen(banner.url.orEmpty())
                        }
                    }
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}


@Composable
private fun CarouselItem(
    modifier: Modifier = Modifier,
    podcastImageUrl: String? = null,
    title: String? = null,
    onUnfollowedClick: () -> Unit,
) {
    Column(
        modifier.padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Box(
            Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally)
                .aspectRatio(1f)
        ) {
            if (podcastImageUrl != null) {
                CoilImage(
                    data = podcastImageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    loading = { /* TODO do something better here */ },
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium)
                )
            }

        }

        if (title != null) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.caption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}