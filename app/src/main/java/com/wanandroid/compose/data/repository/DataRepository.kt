package com.wanandroid.compose.data.repository

import com.wanandroid.compose.Graph
import com.wanandroid.compose.data.api.ApiService
import com.wanandroid.compose.data.bean.ApiResponse
import com.wanandroid.compose.data.bean.ChildrenBean
import com.wanandroid.compose.data.bean.TreeBean
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object DataRepository {

    private val scope = CoroutineScope(Graph.mainDispatcher)

    private val channelDao
        get() = Graph.database.channelDao()

    private val apiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com/")
            .client(Graph.okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)
    }


    suspend fun getArticlesList(curPage: Int, cid: Int? = null) =
        apiService.getArticlesList(curPage, cid)

    suspend fun getBanner() = apiService.getBanner()

    suspend fun getSystem(): ApiResponse<List<TreeBean>> {
        var selectedChannel = channelDao.getChannels()
        return apiService.getSystem().also {
            if (selectedChannel.isNullOrEmpty()) {
                // add default when there is no selected
                it.data?.first()?.children?.let {
                    channelDao.insertAll(it)
                    selectedChannel = it
                }
            }
            //update local selected status
            it.data?.flatMap { it.children }?.map {
                it.apply { selected = selectedChannel.any { it2 -> it.id == it2.id } }
            } ?: emptyList()
        }
    }

    suspend fun addToMyChannel(childrenBean: ChildrenBean) = channelDao.insert(childrenBean)

    suspend fun removeFromMyChannel(childrenBean: ChildrenBean) = channelDao.delete(childrenBean)

    suspend fun login(name: String, pwd: String) = apiService.login(name, pwd)

    suspend fun logout() = apiService.logout()
}