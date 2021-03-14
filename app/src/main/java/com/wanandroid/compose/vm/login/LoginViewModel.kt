package com.wanandroid.compose.vm

import com.wanandroid.compose.data.bean.AccountInfo
import com.wanandroid.compose.data.repository.WanandroidRepository
import com.wanandroid.compose.data.sp.KEY_ACCOUNT
import com.wanandroid.compose.data.sp.WanandroidDataStore
import com.wanandroid.compose.vm.mvi_base.Action
import com.wanandroid.compose.vm.mvi_base.BaseViewModel
import com.wanandroid.compose.vm.mvi_base.ViewState
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

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

    override fun onStart(register: ReducerRegister) {
        register.addReducer(AccountAction::class) { currentViewState, action ->
            when (action) {
                is AccountAction.LoginAction -> {
                    emit(LoginViewState(loading = true))
                    try {
                        val data =
                            requireNotNull(WanandroidRepository.login(action.name, action.pwd).data)
                        emit(LoginViewState(loginInfo = data))
                        WanandroidDataStore.setValue(KEY_ACCOUNT, setOf(action.name, action.pwd))
                    } catch (e: Exception) {
                        emit(currentViewState.copy(loading = false, exception = e))
                    }
                }
                AccountAction.LogoutAction -> {
                    emit(LoginViewState(loading = true))
                    try {
                        if (WanandroidRepository.logout().resultCode == 0) {
                            emit(LoginViewState(loginInfo = null))
                            WanandroidDataStore.setValue(KEY_ACCOUNT, setOf())
                        }
                    } catch (e: Exception) {
                        emit(currentViewState.copy(loading = false, exception = e))
                    }
                }
                AccountAction.AutoLoginAction -> {
                    val loginInfo = WanandroidDataStore.getValueFlow(KEY_ACCOUNT, emptySet())
                        .filter { it.isNotEmpty() && it.size == 2 }.first()
                    dispatch(
                        AccountAction.LoginAction(
                            loginInfo.elementAt(0),
                            loginInfo.elementAt(1)
                        )
                    )
                }
            }
        }
    }
}