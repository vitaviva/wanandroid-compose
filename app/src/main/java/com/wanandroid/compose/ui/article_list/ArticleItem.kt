package com.wanandroid.compose.ui.article_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.wanandroid.compose.ComposeFragment
import com.wanandroid.compose.data.bean.ArticleBean
import com.wanandroid.compose.ui.detail.DetailScreen


@Composable
fun ComposeFragment.ArticleItem(article: ArticleBean) {
    Text(
        modifier = Modifier.height(Dp(100f)).clickable {
            openNewTab {
                DetailScreen(article.link.orEmpty())
            }
        },
        text = "${article.title}"
    )
}
