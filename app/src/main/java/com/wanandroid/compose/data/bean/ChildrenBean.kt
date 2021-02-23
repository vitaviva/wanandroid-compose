package com.wanandroid.compose.data.bean

/**
 * children : []
 * courseId : 13
 * id : 60
 * name : Android Studio相关
 * order : 1000
 * parentChapterId : 150
 * visible : 1
 */
class ChildrenBean(
    val courseId: Int = 0,
    val id: Int = 0,
    val name: String = "",
    val order: Int = 0,
    val parentChapterId: Int = 0,
    val visible: Int = 0,
    val children: List<*>? = null
)