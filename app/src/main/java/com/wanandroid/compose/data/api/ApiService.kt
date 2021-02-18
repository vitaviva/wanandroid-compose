package com.wanandroid.compose.data.api

import com.wanandroid.compose.data.bean.ApiResponse
import com.wanandroid.compose.data.bean.ArticleBean
import com.wanandroid.compose.data.bean.BannerBean
import com.wanandroid.compose.data.bean.Page
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    /**
     * 获取文章列表
     * @param curPage 页码从0开始
     * @return
     */
    @GET("article/list/{curPage}/json")
    suspend fun getArticlesList(@Path("curPage") curPage: Int): ApiResponse<Page<List<ArticleBean>>>

    /**
     * 首页Banner
     * @return
     */
    @GET("banner/json")
    suspend fun getBanner(): ApiResponse<List<BannerBean>>

}
