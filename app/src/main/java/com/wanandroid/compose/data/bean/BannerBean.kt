package com.wanandroid.compose.data.bean

/**
 * desc :
 * id : 6
 * imagePath : http://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png
 * isVisible : 1
 * order : 1
 * title : 我们新增了一个常用导航Tab~
 * type : 0
 * url : http://www.wanandroid.com/navi
 */
data class BannerBean(
    val desc: String? = null,
    val id: Int = 0,
    val imagePath: String? = null,
    val isVisible: Int = 0,
    val order: Int = 0,
    val title: String? = null,
    val type: Int = 0,
    val url: String? = null
)
