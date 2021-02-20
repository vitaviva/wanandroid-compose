package com.wanandroid.compose.vm

import com.wanandroid.compose.data.repository.DataRepository
import com.wanandroid.compose.vm.mvi_base.*
import kotlinx.coroutines.async
import java.lang.Exception


//data class HomeViewState(val list: List<Any>) : ViewState

sealed class HomeAction : Action {
    object Refresh : HomeAction()
    object LoadMore : HomeAction()
}

class HomeViewModel : BaseViewModel<ListingViewState<Any>, Action>(ListingViewState()) {

    override suspend fun LiveCoroutineScope<ListingViewState<Any>>.reduce(
        currentViewState: ListingViewState<Any>,
        action: Action
    ) {
        when (action) {
            is HomeAction.Refresh -> {
                emit(currentViewState.copy(loading = true))
                try {
                    val page = 0
                    val banner = async { loadBanner() }
                    val article = async { loadPage(page) }
                    val list = listOfNotNull(banner.await()) + article.await()
                    emit(ListingViewState(loading = false, page = page, data = list, exception = null))
                } catch (e: Exception) {
                    emit(ListingViewState(exception = e))
                }
            }
            is HomeAction.LoadMore -> {

                try {
                    val page = currentViewState.page + 1
                    val data = currentViewState.data + loadPage(page)
                    emit(currentViewState.copy(page = page, data = data))
                } catch (e: Exception) {
                    emit(ListingViewState(exception = e))
                }

            }
            else -> error("")
        }
    }


    private suspend fun loadPage(page: Int) = DataRepository.getArticlesList(page).data!!.datas

    private suspend fun loadBanner() = DataRepository.getBanner().data!!


}