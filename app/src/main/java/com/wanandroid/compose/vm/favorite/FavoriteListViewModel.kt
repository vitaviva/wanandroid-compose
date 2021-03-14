package com.wanandroid.compose.vm.favorite

import com.github.vitaviva.mvi.PagedListViewModel
import com.wanandroid.compose.data.bean.CollectBean
import com.wanandroid.compose.data.repository.WanandroidRepository
import com.wanandroid.compose.vm.reducer.CollectAction
import com.wanandroid.compose.vm.reducer.collectReducer
import kotlinx.coroutines.CoroutineScope

class FavoriteListViewModel : PagedListViewModel<CollectBean>() {

    override suspend fun CoroutineScope.refresh() = loadPage(0)

    override suspend fun CoroutineScope.loadMore(page: Int) = loadPage(page)

    private suspend fun loadPage(page: Int) = run {
        val t = WanandroidRepository.getCollectList(page)
        t.data.datas
    }

    override fun onStart(register: ReducerRegister) {
        super.onStart(register)
        register.addReducer(CollectAction::class, collectReducer())
    }

}