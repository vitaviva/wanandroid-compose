package com.wanandroid.compose.ui.article_list

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wanandroid.compose.ComposeFragment
import com.wanandroid.compose.data.bean.ArticleBean
import com.wanandroid.compose.ui.detail.DetailScreen


@Composable
fun ComposeFragment.ArticleItem(article: ArticleBean, collect: (Boolean) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
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
        Checkbox(checked = article.collect, onCheckedChange = {
            collect(it)
        })
    }


}
