package com.wanandroid.compose.data.repository

import com.wanandroid.compose.data.api.ApiService
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit


object DataRepository {

    private val apiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com/")
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)
    }


    suspend fun getArticlesList(curPage: Int, cid: Int? = null) =
        apiService.getArticlesList(curPage, cid)

    suspend fun getBanner() = apiService.getBanner()

    suspend fun getSystem() = apiService.getSystem()
}