package com.wanandroid.compose.data.bean

data class ApiResponse<T>(
    val resultCode: Int = RESULT_SUCCESS,
    val errorMsg: String? = null,
    val data: T? = null
) {

    companion object {
        const val RESULT_SUCCESS = 0
    }
}
