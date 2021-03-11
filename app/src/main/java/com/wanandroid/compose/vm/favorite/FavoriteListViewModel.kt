package com.wanandroid.compose.vm.favorite

import com.wanandroid.compose.data.bean.ArticleBean
import com.wanandroid.compose.data.repository.DataRepository
import com.wanandroid.compose.vm.mvi_base.PagedListViewModel
import kotlinx.coroutines.CoroutineScope

class FavoriteListViewModel : PagedListViewModel<ArticleBean>() {

    override suspend fun CoroutineScope.refresh() = loadPage(0)

    override suspend fun CoroutineScope.loadMore(page: Int) = loadPage(page)

    private suspend fun loadPage(page: Int) = run {
        val t = DataRepository.getCollectList(page)
        t.data.datas
    }


}