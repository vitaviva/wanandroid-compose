package com.wanandroid.compose.vm.mvi_base

import androidx.lifecycle.*

abstract class BaseViewModel<S : ViewState, A : Action>(initialState: S) : ViewModel() {
    private val nextAction = MutableLiveData<Pair<A, PostAction>>()
    var viewState: LiveData<S> = MutableLiveData(initialState)

    init {
        viewState = Transformations.switchMap(nextAction) {
            liveData(viewModelScope.coroutineContext) {
                LiveCoroutineScopeImpl(this, viewModelScope)
                    .reduce(viewState.value ?: initialState, it.first)
                it.second.invoke()
            }

        }
    }

    fun dispatch(action: A, after: PostAction = {}) {
        nextAction.value = action to after
    }

    abstract suspend fun LiveCoroutineScope<S>.reduce(currentViewState: S, action: A)
}

typealias PostAction = suspend () -> Unit

interface Action

interface ViewState
