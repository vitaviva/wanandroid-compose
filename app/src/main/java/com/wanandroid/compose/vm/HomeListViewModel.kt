package com.wanandroid.compose.vm

import com.wanandroid.compose.data.repository.DataRepository
import com.wanandroid.compose.vm.mvi_base.PagedListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

class HomeListViewModel : PagedListViewModel<Any>() {

    override suspend fun CoroutineScope.refresh(): List<Any> {
        val banner = async { loadBanner() }
        val article = async { loadPage(0) }
        return listOfNotNull(banner.await()) + article.await()
    }

    override suspend fun CoroutineScope.loadMore(page: Int): List<Any> {
        return loadPage(page)
    }

    private suspend fun loadPage(page: Int) = DataRepository.getArticlesList(page).data!!.datas

    private suspend fun loadBanner() = DataRepository.getBanner().data!!


}