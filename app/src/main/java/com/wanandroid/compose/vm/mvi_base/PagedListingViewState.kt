package com.wanandroid.compose.vm.mvi_base

data class PagedListingViewState<T>(
    val loading: Boolean = false,
    val page: Int = 0,
    val data: List<T> = emptyList(),
    val exception: Exception? = null
) : ViewState {
    /**
     * True if this contains an error
     */
    val hasError: Boolean
        get() = exception != null

    /**
     * True if this represents a first load
     */
    val initialLoad: Boolean
        get() = data.isEmpty() && loading && !hasError
}


