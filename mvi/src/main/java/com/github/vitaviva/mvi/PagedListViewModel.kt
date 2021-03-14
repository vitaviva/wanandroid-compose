package com.github.vitaviva.mvi

import kotlinx.coroutines.CoroutineScope

abstract class PagedListViewModel<T> :
    BaseViewModel<PagedListingViewState<T>, Action>(PagedListingViewState()) {

    override fun onStart(register: ReducerRegister) {
        register.addReducer(PagedListingAction::class) { currentViewState, action ->
            when (action) {
                is PagedListingAction.Refresh -> {
                    emit(currentViewState.copy(loading = true))
                    try {
                        val res = refresh()
                        emit(
                            PagedListingViewState(
                                loading = false,
                                page = 0,
                                data = res,
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
    }


    abstract suspend fun CoroutineScope.refresh(): List<T>

    abstract suspend fun CoroutineScope.loadMore(page: Int): List<T>

}