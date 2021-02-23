package com.wanandroid.compose.vm

import com.wanandroid.compose.data.bean.ChildrenBean
import com.wanandroid.compose.data.repository.DataRepository
import com.wanandroid.compose.vm.mvi_base.Action
import com.wanandroid.compose.vm.mvi_base.BaseViewModel
import com.wanandroid.compose.vm.mvi_base.LiveCoroutineScope
import com.wanandroid.compose.vm.mvi_base.ViewState

data class ChannelViewState(
    val allChannels: List<ChildrenBean> = emptyList(),
    val curChannel: ChildrenBean? = null,
) : ViewState


sealed class ChannelAction : Action {
    object LoadAllChannels : Action
    data class SwitchChannel(val channel: ChildrenBean) : Action
}

class ChannelTabViewModel : BaseViewModel<ChannelViewState, Action>(ChannelViewState()) {

    override suspend fun LiveCoroutineScope<ChannelViewState>.reduce(
        currentViewState: ChannelViewState,
        action: Action
    ) {
        when (action) {
            is ChannelAction.LoadAllChannels -> {
                val channels = DataRepository.getSystem().data!![0].children
                emit(
                    currentViewState.copy(
                        curChannel = channels[0],
                        allChannels = channels
                    )
                )
            }
            is ChannelAction.SwitchChannel -> {
                emit(currentViewState.copy(curChannel = action.channel))
            }
            else -> error("")
        }
    }

}
