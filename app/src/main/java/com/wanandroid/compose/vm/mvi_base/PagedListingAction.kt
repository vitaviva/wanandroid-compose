package com.wanandroid.compose.vm.mvi_base


sealed class PagedListingAction : Action {
    object Refresh : PagedListingAction()
    object LoadMore : PagedListingAction()
}
