package com.wanandroid.compose.vm

import com.wanandroid.compose.data.repository.WanandroidRepository
import com.wanandroid.compose.vm.mvi_base.PagedListViewModel
import com.wanandroid.compose.vm.reducer.CollectAction
import com.wanandroid.compose.vm.reducer.collectReducer
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

    override fun onStart(register: ReducerRegister) {
        super.onStart(register)
        register.addReducer(CollectAction::class, collectReducer())
    }

    private suspend fun loadPage(page: Int) = WanandroidRepository.getArticlesList(page).data.datas

    private suspend fun loadBanner() = WanandroidRepository.getBanner().data


}