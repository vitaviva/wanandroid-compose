package com.wanandroid.compose.vm

import com.wanandroid.compose.data.bean.ArticleBean
import com.wanandroid.compose.data.repository.DataRepository
import com.wanandroid.compose.vm.mvi_base.PagedListViewModel
import kotlinx.coroutines.CoroutineScope

class ChannelListViewModel(private val cid: Int) : PagedListViewModel<ArticleBean>() {

    override suspend fun CoroutineScope.refresh(): List<ArticleBean> = loadPage(0)

    override suspend fun CoroutineScope.loadMore(page: Int): List<ArticleBean> =  loadPage(page)

    private suspend fun loadPage(page: Int) = DataRepository.getArticlesList(page, cid).data!!.datas


}