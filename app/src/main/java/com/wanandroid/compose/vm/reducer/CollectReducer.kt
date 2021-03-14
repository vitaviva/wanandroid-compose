package com.wanandroid.compose.vm.reducer

import com.wanandroid.compose.data.bean.ArticleBean
import com.wanandroid.compose.data.repository.WanandroidRepository
import com.wanandroid.compose.vm.mvi_base.Action
import com.wanandroid.compose.vm.mvi_base.PagedListingViewState
import com.wanandroid.compose.vm.mvi_base.Reducer

data class CollectAction(val id: Int, val select: Boolean) : Action

fun <T> collectReducer(): Reducer<PagedListingViewState<T>, CollectAction> =
    { currentViewState, action ->
        lateinit var data: List<T>
        if (action.select) {
            if (WanandroidRepository.collect(action.id).resultCode == 0) {
                data = currentViewState.data.map {
                    if (it is ArticleBean)
                        if (it.id == action.id) it.copy(collect = true) else it
                    else it
                } as List<T>
            }
        } else {
            if (WanandroidRepository.unCollect(action.id).resultCode == 0) {
                data = currentViewState.data.map {
                    if (it is ArticleBean)
                        if (it.id == action.id) it.copy(collect = false) else it
                    else it
                } as List<T>
            }
        }
        emit(
            currentViewState.copy(data = data)
        )
    }