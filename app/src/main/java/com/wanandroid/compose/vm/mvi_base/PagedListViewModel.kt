package com.wanandroid.compose.vm.mvi_base

import kotlinx.coroutines.CoroutineScope

abstract class PagedListViewModel<T> :
    BaseViewModel<PagedListingViewState<T>, PagedListingAction>(PagedListingViewState()) {

    final override suspend fun LiveCoroutineScope<PagedListingViewState<T>>.reduce(
        currentViewState: PagedListingViewState<T>,
        action: PagedListingAction
    ) {
        when (action) {
            is PagedListingAction.Refresh -> {
                emit(currentViewState.copy(loading = true))
                try {
                    emit(
                        PagedListingViewState(
                            loading = false,
                            page = 0,
                            data = refresh(),
                            exception = null
                        )
                    )
                } catch (e: Exception) {
                    emit(PagedListingViewState(exception = e))
                }
            }
            is PagedListingAction.LoadMore -> {
                try {
                    val page = currentViewState.page + 1
                    val data = currentViewState.data + loadMore(page)
                    emit(currentViewState.copy(page = page, data = data))
                } catch (e: Exception) {
                    emit(PagedListingViewState(exception = e))
                }

            }
        }

    }

    abstract suspend fun CoroutineScope.refresh(): List<T>

    abstract suspend fun CoroutineScope.loadMore(page: Int): List<T>

}