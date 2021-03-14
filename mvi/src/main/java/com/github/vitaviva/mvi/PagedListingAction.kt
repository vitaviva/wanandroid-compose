package com.github.vitaviva.mvi


sealed class PagedListingAction : Action {
    object Refresh : PagedListingAction()
    object LoadMore : PagedListingAction()
}
