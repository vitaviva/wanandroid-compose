package com.wanandroid.compose.data.bean

import androidx.compose.runtime.Immutable
import androidx.room.*

/**
 * children : []
 * courseId : 13
 * id : 60
 * name : Android Studio相关
 * order : 1000
 * parentChapterId : 150
 * visible : 1
 */

@Entity(
    tableName = "channels",
    indices = [
        Index("name", unique = true)
    ]
)
data class ChildrenBean(
    @Ignore val courseId: Int = 0,
    @PrimaryKey @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "parent_id") var parentChapterId: Int = 0,
    @Ignore val order: Int = 0,
    @Ignore val visible: Int = 0,
    @Ignore val children: List<*>? = null,
    @ColumnInfo(name = "selected") var selected: Boolean = false,
)