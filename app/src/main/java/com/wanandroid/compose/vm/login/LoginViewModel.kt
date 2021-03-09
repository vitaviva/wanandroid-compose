package com.wanandroid.compose.vm

import com.wanandroid.compose.data.bean.Login
import com.wanandroid.compose.data.repository.DataRepository
import com.wanandroid.compose.vm.mvi_base.Action
import com.wanandroid.compose.vm.mvi_base.BaseViewModel
import com.wanandroid.compose.vm.mvi_base.LiveCoroutineScope
import com.wanandroid.compose.vm.mvi_base.ViewState

data class LoginViewState(
    val loading: Boolean = false,
    val loginInfo: Login? = null,
    val exception: Exception? = null
) : ViewState


data class LoginAction(val name: String, val pwd: String) : Action

class LoginViewModel : BaseViewModel<LoginViewState, LoginAction>(LoginViewState()) {
    override suspend fun LiveCoroutineScope<LoginViewState>.reduce(
        currentViewState: LoginViewState,
        action: LoginAction
    ) {
        emit(LoginViewState(loading = true))
        try {
            val data = requireNotNull(DataRepository.login(action.name, action.pwd).data)
            emit(LoginViewState(loginInfo = data))
        } catch (e: Exception) {
            emit(currentViewState.copy(loading = false, exception = e))
        }
    }
}