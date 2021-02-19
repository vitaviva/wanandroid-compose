package com.wanandroid.compose.mvi.base

import androidx.lifecycle.*

abstract class BaseViewModel<S : ViewState, A : Action>(initialState: S) : ViewModel() {
    private val nextAction = MutableLiveData<A>()
    var viewState: LiveData<S> = MutableLiveData(initialState)

    init {
        viewState = Transformations.switchMap(nextAction) {
            liveData {
                LiveCoroutineScopeImpl(this, viewModelScope)
                    .reduce(viewState.value ?: initialState, it)
            }

        }
    }

    fun dispatch(action: A) {
        nextAction.value = action
    }

    abstract suspend fun LiveCoroutineScope<S>.reduce(currentViewState: S, action: A)
}

interface Action

interface ViewState
