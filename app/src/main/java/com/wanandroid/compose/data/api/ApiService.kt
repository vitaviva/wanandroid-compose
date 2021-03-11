package com.wanandroid.compose.data.api

import com.wanandroid.compose.data.bean.*
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    /**
     * 获取文章列表
     * @param curPage 页码从0开始
     * @return
     */
    @GET("article/list/{curPage}/json")
    suspend fun getArticlesList(
        @Path("curPage") curPage: Int,
        @Query("cid") id: Int? = null
    ): ApiResponse<Page<List<ArticleBean>>>

    /**
     * 首页Banner
     * @ return
     */
    @GET("banner/json")
    suspend fun getBanner(): ApiResponse<List<BannerBean>>


    /**
     * 知识体系
     * @return
     */
    @GET("tree/json")
    suspend fun getSystem(): ApiResponse<List<TreeBean>>


    /**
     * 登陆
     */
    @POST("user/login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): ApiResponse<AccountInfo>


    /**
     * 登出
     */
    @GET("user/logout/json")
    suspend fun logout(): ApiResponse<Any>


    /**
     * 获取收藏列表
     * @param curPage 从第0页开始
     * @return
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectList(@Path("page") page: Int): ApiResponse<Page<List<ArticleBean>>>

}
