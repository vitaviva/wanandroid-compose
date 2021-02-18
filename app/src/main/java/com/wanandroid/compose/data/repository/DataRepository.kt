package com.wanandroid.compose.data.repository

import com.wanandroid.compose.data.api.ApiService
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit


object DataRepository {

    private val api by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com/")
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    suspend fun getArticlesList(curPage: Int) =
        api.create(ApiService::class.java).getArticlesList(curPage)


    suspend fun getBanner() =
        api.create(ApiService::class.java).getBanner()
}