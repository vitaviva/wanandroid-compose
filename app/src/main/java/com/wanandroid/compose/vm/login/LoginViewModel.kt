package com.wanandroid.compose.vm

import com.wanandroid.compose.data.bean.AccountInfo
import com.wanandroid.compose.data.repository.DataRepository
import com.wanandroid.compose.data.sp.KEY_ACCOUNT
import com.wanandroid.compose.data.sp.WanandroidDataStore
import com.wanandroid.compose.vm.mvi_base.Action
import com.wanandroid.compose.vm.mvi_base.BaseViewModel
import com.wanandroid.compose.vm.mvi_base.LiveCoroutineScope
import com.wanandroid.compose.vm.mvi_base.ViewState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

data class LoginViewState(
    val loading: Boolean = false,
    val loginInfo: AccountInfo? = null,
    val exception: Exception? = null
) : ViewState


sealed class AccountAction : Action {
    data class LoginAction(val name: String, val pwd: String) : AccountAction()
    object AutoLoginAction : AccountAction()
    object LogoutAction : AccountAction()
}


class LoginViewModel : BaseViewModel<LoginViewState, AccountAction>(LoginViewState()) {
    override suspend fun LiveCoroutineScope<LoginViewState>.reduce(
        currentViewState: LoginViewState,
        action: AccountAction
    ) {
        when (action) {
            is AccountAction.LoginAction -> {
                emit(LoginViewState(loading = true))
                try {
                    val data = requireNotNull(DataRepository.login(action.name, action.pwd).data)
                    emit(LoginViewState(loginInfo = data))
                    WanandroidDataStore.setValue(KEY_ACCOUNT, setOf(action.name, action.pwd))
                } catch (e: Exception) {
                    emit(currentViewState.copy(loading = false, exception = e))
                }
            }
            AccountAction.LogoutAction -> {
                emit(LoginViewState(loading = true))
                try {
                    if (DataRepository.logout().resultCode == 0) {
                        emit(LoginViewState(loginInfo = null))
                        WanandroidDataStore.setValue(KEY_ACCOUNT, setOf())
                    }
                } catch (e: Exception) {
                    emit(currentViewState.copy(loading = false, exception = e))
                }
            }
            AccountAction.AutoLoginAction -> {
                WanandroidDataStore.getValueFlow(KEY_ACCOUNT, emptySet())
                    .filter { it.isNotEmpty() && it.size == 2 }
                    .collect {
                        dispatch(AccountAction.LoginAction(it.elementAt(0), it.elementAt(1)))
                    }
            }
        }

    }
}