package com.wanandroid.compose.vm

import com.wanandroid.compose.data.bean.ChildrenBean
import com.wanandroid.compose.data.bean.TreeBean
import com.wanandroid.compose.data.repository.WanandroidRepository
import com.wanandroid.compose.vm.mvi_base.Action
import com.wanandroid.compose.vm.mvi_base.BaseViewModel
import com.wanandroid.compose.vm.mvi_base.ViewState

data class ChannelViewState(
    val allChannels: List<TreeBean> = emptyList(),
    val curChannel: ChildrenBean? = null,
) : ViewState

val ChannelViewState.myChannels
    get() = allChannels.flatMap { it.children }.filter { it.selected }


sealed class ChannelAction : Action {
    object LoadAllChannels : ChannelAction()
    data class SwitchChannel(val channel: ChildrenBean) : ChannelAction()
    data class AddChannel(val channel: ChildrenBean) : ChannelAction()
    data class RemoveChannel(val channel: ChildrenBean) : ChannelAction()
}


class ChannelTabViewModel : BaseViewModel<ChannelViewState, ChannelAction>(ChannelViewState()) {

    private fun List<TreeBean>.setChannelSelected(
        channel: ChildrenBean,
        selected: Boolean
    ): List<TreeBean> {
        return map { parent ->
            if (parent.id == channel.parentChapterId) {
                parent.copy(children = parent.children.map {
                    if (it.id == channel.id) {
                        it.copy(selected = selected)
                    } else {
                        it
                    }
                })
            } else parent
        }
    }

    override fun onStart(register: ReducerRegister) {
        register.addReducer(ChannelAction::class) { currentViewState, action ->
            when (action) {
                ChannelAction.LoadAllChannels -> {
                    val trees = WanandroidRepository.getSystem().data
                    val myChannels = trees.flatMap { it.children }.filter { it.selected }
                    val curChannel = currentViewState.curChannel ?: myChannels.first()

                    emit(
                        currentViewState.copy(
                            allChannels = trees,
                            curChannel = curChannel
                        )
                    )
                }
                is ChannelAction.SwitchChannel -> {
                    emit(currentViewState.copy(curChannel = action.channel))
                }
                is ChannelAction.AddChannel -> {
                    emit(
                        currentViewState.copy(
                            allChannels = currentViewState.allChannels.setChannelSelected(
                                action.channel, true
                            )
                        ).also { WanandroidRepository.addToMyChannel(action.channel) }
                    )
                }
                is ChannelAction.RemoveChannel -> {
                    emit(
                        currentViewState.copy(
                            allChannels = currentViewState.allChannels.setChannelSelected(
                                action.channel, false
                            )
                        ).also { WanandroidRepository.removeFromMyChannel(action.channel) }
                    )
                }
            }
        }
    }

}
