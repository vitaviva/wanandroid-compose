package com.wanandroid.compose.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanandroid.compose.data.repository.DataRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    internal var curPage: Int = 0
//    var list: LiveData<List<ArticleBean>> = liveData {
//        val list = DataRepository.getArticlesList(0).data!!.datas
//        emit(list)
//    }


    private var _refreshing = MutableLiveData(RequestStatus.SUCCESS)

    private val _articleList: MutableLiveData<List<Any>> = MutableLiveData()
    val articleList: LiveData<List<Any>>
        get() = _articleList

    fun refresh(cb: suspend () -> Unit) {

        viewModelScope.launch {
            _refreshing.postValue(RequestStatus.REFRESHING)
            val banner = async { loadBanner() }
            val article = async {
                curPage = 0
                loadCurPage()
            }
            val list = listOf<Any>(banner.await()) + article.await()
            _articleList.postValue(list)
            cb.invoke()
            _refreshing.postValue(RequestStatus.SUCCESS)
        }

    }

    fun loadMore() {
        viewModelScope.launch {
            _articleList.postValue(
                (_articleList.value ?: emptyList()) + loadCurPage()
            )
        }
    }

    private suspend fun loadCurPage() = DataRepository.getArticlesList(curPage++).data!!.datas

    val isRefreshing: LiveData<RequestStatus>
        get() = _refreshing


    private suspend fun loadBanner() = DataRepository.getBanner().data!!

}

enum class RequestStatus {
    REFRESHING, SUCCESS, ERROR
}