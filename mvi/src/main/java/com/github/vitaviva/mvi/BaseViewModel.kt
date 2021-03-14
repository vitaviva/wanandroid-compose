package com.github.vitaviva.mvi

import androidx.lifecycle.*
import kotlin.reflect.KClass

abstract class BaseViewModel<S : ViewState, A : Action>(initialState: S) : ViewModel() {
    private val nextAction = MutableLiveData<Pair<A, PostAction>>()
    lateinit var viewState: LiveData<S>

    private val reducerMap: Map<KClass<out A>, Reducer<S, Action>> by lazy {
        ReducerRegister().also { onStart(it) }.map
    }

    protected inner class ReducerRegister {
        internal val map = mutableMapOf<KClass<out A>, Reducer<S, Action>>()
        fun <T : A> addReducer(
            action: KClass<T>,
            reducer: Reducer<S, T>
        ) {
            map.keys.forEach {
                if (it == action || it.java.isAssignableFrom(action.java)) {
                    error("DON'T add reducer with same action repeatedly")
                }
            }
            map[action] = reducer as Reducer<S, Action>
        }
    }

    protected abstract fun onStart(register: ReducerRegister)

    fun dispatch(action: A, after: PostAction = {}) {
        nextAction.value = action to after
    }

    private fun reducerOf(action: A) =
        reducerMap.entries.first {
            (it == action || it.key.java.isAssignableFrom(action.javaClass))
        }.value


    init {
        viewState = Transformations.switchMap(nextAction) {
            liveData(viewModelScope.coroutineContext) {
                val reducer = reducerOf(it.first)
                reducer.invoke(
                    LiveCoroutineScopeImpl(this@liveData, viewModelScope),
                    viewState.value ?: initialState,
                    it.first
                )
                it.second.invoke()
            }
        }
    }

}

typealias PostAction = suspend () -> Unit

interface Action

interface ViewState

typealias Reducer<S, A> = suspend LiveCoroutineScope<S>.(currentViewState: S, action: A) -> Unit
