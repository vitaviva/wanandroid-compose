package com.wanandroid.compose.mvi.vm

import com.wanandroid.compose.data.repository.DataRepository
import com.wanandroid.compose.mvi.base.Action
import com.wanandroid.compose.mvi.base.LiveCoroutineScope
import com.wanandroid.compose.mvi.base.BaseViewModel
import com.wanandroid.compose.mvi.base.ListingState
import kotlinx.coroutines.async
import java.lang.Exception


//data class HomeViewState(val list: List<Any>) : ViewState

sealed class HomeAction : Action {
    object Refresh : HomeAction()
    object LoadMore : HomeAction()
}

class HomeViewModel : BaseViewModel<ListingState<Any>, Action>(ListingState.Success()) {

    override suspend fun LiveCoroutineScope<ListingState<Any>>.reduce(
        currentViewState: ListingState<Any>,
        action: Action
    ) {
        when (action) {
            is HomeAction.Refresh -> {
                emit(ListingState.Loading)
                try {
                    val page = 0
                    val banner = async { loadBanner() }
                    val article = async { loadPage(page) }
                    val list = listOfNotNull(banner.await()) + article.await()
                    emit(ListingState.Success(page, list))
                } catch (e: Exception) {
                    emit(ListingState.Fail(e.message ?: "no data"))
                }
            }
            is HomeAction.LoadMore -> {
                val cur = currentViewState as ListingState.Success
                val page = cur.page + 1
                try {
                    emit(cur.copy(page = page, list = cur.list + loadPage(page)))
                } catch (e: Exception) {
                    emit(ListingState.Fail(e.message ?: "no data"))
                }

            }
            else -> error("")
        }
    }


    private suspend fun loadPage(page: Int) = DataRepository.getArticlesList(page).data!!.datas

    private suspend fun loadBanner() = DataRepository.getBanner().data!!


}