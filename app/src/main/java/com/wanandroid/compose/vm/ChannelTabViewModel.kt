package com.wanandroid.compose.vm

import com.wanandroid.compose.data.bean.ChildrenBean
import com.wanandroid.compose.data.bean.TreeBean
import com.wanandroid.compose.data.repository.DataRepository
import com.wanandroid.compose.vm.mvi_base.Action
import com.wanandroid.compose.vm.mvi_base.BaseViewModel
import com.wanandroid.compose.vm.mvi_base.LiveCoroutineScope
import com.wanandroid.compose.vm.mvi_base.ViewState
import java.util.*

data class ChannelViewState(
    val allChannels: List<TreeBean> = emptyList(),
    val myChannels: List<ChildrenBean>? = null,
    val curChannel: ChildrenBean? = null,
) : ViewState


sealed class ChannelAction : Action {
    object LoadAllChannels : Action
    data class SwitchChannel(val channel: ChildrenBean) : Action
    data class AddChannel(val channel: ChildrenBean) : Action
    data class RemoveChannel(val channel: ChildrenBean) : Action
}

class ChannelTabViewModel : BaseViewModel<ChannelViewState, Action>(ChannelViewState()) {

    override suspend fun LiveCoroutineScope<ChannelViewState>.reduce(
        currentViewState: ChannelViewState,
        action: Action
    ) {
        when (action) {
            ChannelAction.LoadAllChannels -> {
                val trees = requireNotNull(DataRepository.getSystem().data)
                val myChannels = currentViewState.myChannels ?: trees.first().children
                val curChannel = currentViewState.curChannel ?: myChannels.first()

                emit(
                    currentViewState.copy(
                        allChannels = trees,
                        myChannels = myChannels,
                        curChannel = curChannel
                    ).also { it.updateToMap() }
                )
            }
            is ChannelAction.SwitchChannel -> {
                emit(currentViewState.copy(curChannel = action.channel))
            }
            is ChannelAction.AddChannel -> {
                emit(
                    currentViewState.copy(
                        myChannels = currentViewState.myChannels?.plus(action.channel)
                    ).also { it.updateToMap() }
                )
            }
            is ChannelAction.RemoveChannel -> {
                emit(
                    currentViewState.copy(
                        myChannels = currentViewState.myChannels?.minus(action.channel)
                    ).also { it.updateToMap() }
                )
            }
        }
    }

}


private val _map = WeakHashMap<ChildrenBean, Boolean>()
private fun ChannelViewState.updateToMap() {
    _map.clear()
    myChannels?.forEach {
        _map[it] = true
    }
}
fun ChildrenBean.isSelected() = run { this in _map }
