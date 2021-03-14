package com.github.vitaviva.mvi

import androidx.lifecycle.LiveDataScope
import kotlinx.coroutines.CoroutineScope


interface LiveCoroutineScope<T> : LiveDataScope<T>, CoroutineScope

class LiveCoroutineScopeImpl<T : ViewState>(
    liveDataScope: LiveDataScope<T>,
    coroutineScope: CoroutineScope
) : LiveCoroutineScope<T>,
    LiveDataScope<T> by liveDataScope,
    CoroutineScope by coroutineScope
