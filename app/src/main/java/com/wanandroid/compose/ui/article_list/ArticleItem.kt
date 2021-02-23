package com.wanandroid.compose.ui.article_list

import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.wanandroid.compose.data.bean.ArticleBean


@Composable
fun ArticleItem(article: ArticleBean) {
    Text(
        modifier = Modifier.height(Dp(100f)),
        text = "${article.title}"
    )
}
