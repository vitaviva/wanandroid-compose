package com.wanandroid.compose.vm

import com.github.vitaviva.mvi.PagedListViewModel
import com.wanandroid.compose.data.bean.ArticleBean
import com.wanandroid.compose.data.repository.WanandroidRepository
import com.wanandroid.compose.vm.reducer.CollectAction
import com.wanandroid.compose.vm.reducer.collectReducer
import kotlinx.coroutines.CoroutineScope

class ChannelListViewModel(private val cid: Int) : PagedListViewModel<ArticleBean>() {

    override suspend fun CoroutineScope.refresh(): List<ArticleBean> = loadPage(0)

    override suspend fun CoroutineScope.loadMore(page: Int): List<ArticleBean> = loadPage(page)

    private suspend fun loadPage(page: Int) =
        WanandroidRepository.getArticlesList(page, cid).data.datas


    override fun onStart(register: ReducerRegister) {
        super.onStart(register)
        register.addReducer(CollectAction::class, collectReducer())
    }
}