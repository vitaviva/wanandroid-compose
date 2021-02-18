package com.wanandroid.compose.ui.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.AmbientContentAlpha
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AmbientAnimationClock
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.wanandroid.compose.data.bean.ArticleBean
import com.wanandroid.compose.data.bean.BannerBean
import com.wanandroid.compose.utils.Fab
import com.wanandroid.compose.utils.PagedList
import com.wanandroid.compose.utils.Pager
import com.wanandroid.compose.utils.PagerState
import com.wanandroid.compose.vm.HomeViewModel
import com.wanandroid.compose.vm.RequestStatus
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun Fragment.Home(
    modifier: Modifier = Modifier,
    navBackStackEntry: NavBackStackEntry,
    navHostController: NavHostController
) {
    val vm: HomeViewModel by remember { viewModels() }
    val articleList by vm.articleList.observeAsState(initial = emptyList())
    val refreshState = remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    val pagerState = run {
        val clock = AmbientAnimationClock.current
        remember(clock) { PagerState(clock) }
    }


    val onRefresh = remember {
        {
            vm.refresh {
                lazyListState.snapToItemIndex(0)
                pagerState.currentPage = 0
            }
        }
    }

    DisposableEffect(key1 = "") {
        val observer = { it: RequestStatus ->
            refreshState.value = it == RequestStatus.REFRESHING
        }
        vm.isRefreshing.observe(this@Home, observer)
        onRefresh.invoke()
        onDispose {
            vm.isRefreshing.removeObserver(observer)
        }
    }

    Box(modifier.fillMaxSize()) {

        PagedList(
            datas = articleList,
            listState = lazyListState,
            onLoadMore = remember {
                {
                    vm.loadMore()
                    Toast.makeText(
                        context,
                        "loadMore: ${vm.curPage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        ) { index, item ->

            when (item) {
                is List<*> ->
                    run {

                        pagerState.maxPage = (item.size - 1).coerceAtLeast(0)

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
                                val banner = item[page] as BannerBean
                                Log.e("wangp", "banner:${banner.title}")

//                                Text("${banner.title}")
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
                is ArticleBean ->
                    Text(
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.height(Dp(100f)),
                        text = "${item.title}"
                    )
                else -> error("")
            }

        }

        Fab(action = onRefresh)
    }


}


@Composable
private fun FollowedPodcastCarouselItem(
    modifier: Modifier = Modifier,
    podcastImageUrl: String? = null,
    title: String? = null,
    onUnfollowedClick: () -> Unit,
) {
//    Text(title ?: "hahaha")
    Column(
        modifier.padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Box(
            Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally)
                .aspectRatio(1f)
        ) {
            Log.e("wangp", "title:$title")
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

