package com.wanandroid.compose.data.bean

/**
 * children : [{"children":[],"courseId":13,"id":60,"name":"Android Studio相关","order":1000,"parentChapterId":150,"visible":1},{"children":[],"courseId":13,"id":169,"name":"gradle","order":1001,"parentChapterId":150,"visible":1},{"children":[],"courseId":13,"id":269,"name":"官方发布","order":1002,"parentChapterId":150,"visible":1}]
 * courseId : 13
 * id : 150
 * name : 开发环境
 * order : 1
 * parentChapterId : 0
 * visible : 1
 */
data class TreeBean(
    val courseId : Int = 0,
    val id :Int= 0,
    val name: String? = null,
    val order :Int = 0,
    val parentChapterId:Int = 0,
    val visible : Int = 0,
    val children: List<ChildrenBean> = emptyList()
)