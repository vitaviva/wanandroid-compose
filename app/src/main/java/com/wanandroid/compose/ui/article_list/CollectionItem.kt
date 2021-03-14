package com.wanandroid.compose.ui.article_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wanandroid.compose.ComposeFragment
import com.wanandroid.compose.data.bean.CollectBean
import com.wanandroid.compose.ui.detail.DetailScreen


@Composable
fun ComposeFragment.CollectionItem(article: CollectBean, unCollect: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            modifier = Modifier
                .height(Dp(100f))
                .weight(1f)
                .clickable {
                    openNewTab {
                        DetailScreen(article.link.orEmpty())
                    }
                },
            text = "${article.title}"
        )
        Button(onClick = {
            unCollect.invoke()
        }) {
            Text("取消收藏")
        }
    }


}
