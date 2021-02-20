package com.wanandroid.compose.ui.home

import ContentLoadingLayout
import android.widget.Toast
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
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.wanandroid.compose.components.FullScreenLoading
import com.wanandroid.compose.components.Pager
import com.wanandroid.compose.components.PagerState
import com.wanandroid.compose.data.bean.ArticleBean
import com.wanandroid.compose.data.bean.BannerBean
import com.wanandroid.compose.vm.mvi_base.ListingViewState
import com.wanandroid.compose.vm.HomeAction
import com.wanandroid.compose.vm.HomeViewModel
import com.wanandroid.compose.utils.Fab
import com.wanandroid.compose.utils.PagedList
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun Fragment.Home(
    modifier: Modifier = Modifier,
    navBackStackEntry: NavBackStackEntry,
    navHostController: NavHostController
) {

    val vm: HomeViewModel by remember { viewModels() }
    val viewState by vm.viewState.observeAsState(ListingViewState())
    val listState = rememberLazyListState()
    val pagerState = remember { PagerState() }

    val onRefresh = remember {
        {
            vm.dispatch(HomeAction.Refresh) {
                listState.snapToItemIndex(0)
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
private fun HomeList(
    modifier: Modifier = Modifier,
    vm: HomeViewModel,
    pagerState: PagerState = remember { PagerState() },
    listState: LazyListState = rememberLazyListState()
) {

    val viewState by vm.viewState.observeAsState(ListingViewState())
    val context = AmbientContext.current

    PagedList(
        modifier = modifier,
        datas = viewState.data,
        listState = listState,
        onLoadMore = remember(viewState.page) {
            {
                vm.dispatch(HomeAction.LoadMore)
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
                ArticleItem(item)

            else -> error("")
        }

    }

}


@Composable
private fun ArticleItem(article: ArticleBean) {
    Text(
        modifier = Modifier.height(Dp(100f)),
        text = "${article.title}"
    )
}


@Composable
private fun BannerItem(pagerState: PagerState, list: List<BannerBean>) {

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
                .preferredHeight(200.dp)
        ) {
            val banner = list[page]

            FollowedPodcastCarouselItem(
                podcastImageUrl = banner.imagePath,
                title = banner.title,
                onUnfollowedClick = { /*onPodcastUnfollowed(podcast.uri)*/ },
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxHeight()
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun FollowedPodcastCarouselItem(
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
            Providers(AmbientContentAlpha provides ContentAlpha.medium) {
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

