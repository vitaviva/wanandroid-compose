package com.wanandroid.compose.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.wanandroid.compose.data.bean.ArticleBean

@Composable
fun <T> PagedList(
    modifier: Modifier = Modifier,
    datas: List<T>,
    listState: LazyListState = rememberLazyListState(),
    onLoadMore: () -> Unit,
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit,
) {
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize()
    ) {
        itemsIndexed(datas) { index, item ->

            itemContent(index, item)

            DisposableEffect(datas.size) {
                if (datas.size > CNT_PER_PAGE && datas.size - index < 2) {
                    onLoadMore()
                }
                onDispose { }
            }

        }
    }
}

private const val CNT_PER_PAGE = 8
