package com.wanandroid.compose.mvi.base

sealed class ListingState<out T> : ViewState {
    object Loading : ListingState<Unit>()
    data class Success<T>(val page: Int = 0, val list: List<T> = emptyList()) : ListingState<T>()
    data class Fail(val errors: String = "no data") : ListingState<Unit>()
}