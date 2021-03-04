package com.wanandroid.compose.ui.channel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wanandroid.compose.components.FlowLayout
import com.wanandroid.compose.data.bean.ChildrenBean
import com.wanandroid.compose.vm.ChannelAction
import com.wanandroid.compose.vm.ChannelTabViewModel
import com.wanandroid.compose.vm.ChannelViewState
import com.wanandroid.compose.vm.myChannels

@Composable
fun ChannelChooser(vm: ChannelTabViewModel) {

    val viewState by vm.viewState.observeAsState(ChannelViewState())

    Spacer(modifier = Modifier.height(20.dp))

    LazyColumn {
        item {
            ChannelFlow(
                "我的频道",
                viewState.myChannels
            ) {
                vm.dispatch(ChannelAction.RemoveChannel(it))
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        items(viewState.allChannels.size) {
            val tree = viewState.allChannels[it]
            ChannelFlow(tree.name ?: "", tree.children) { channel ->
                if (channel.selected) {
                    vm.dispatch(ChannelAction.RemoveChannel(channel))
                } else {
                    vm.dispatch(ChannelAction.AddChannel(channel))
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}


@Composable
private fun ChannelFlow(
    channelName: String,
    channels: List<ChildrenBean>,
    onItemClick: (ChildrenBean) -> Unit
) {

    Column(Modifier.padding(10.dp)) {

        Text(channelName)

        Divider(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp))


        FlowLayout {
            channels.forEach {
                FlowItem(text = it.name, selected = it.selected) { onItemClick(it) }
                Spacer(Modifier.size(5.dp))
            }
        }
    }

}


@Composable
private fun FlowItem(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        color = when {
            selected -> MaterialTheme.colors.primary.copy(alpha = 0.08f)
            else -> MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
        },
        contentColor = when {
            selected -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.onSurface
        },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .padding(horizontal = 4.dp, vertical = 16.dp)
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}


@Preview("Flow Preview")
@Composable
fun PreviewFlowLayout() {
    FlowLayout(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        val channel = ChildrenBean(name = "test")
        FlowItem(text = channel.name, selected = false) {}

        FlowItem(text = channel.name, selected = true) {}
    }
}
