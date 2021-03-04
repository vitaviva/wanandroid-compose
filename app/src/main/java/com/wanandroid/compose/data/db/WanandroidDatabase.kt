package com.wanandroid.compose.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wanandroid.compose.data.bean.ChildrenBean

@Database(
    entities = [
        ChildrenBean::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class WanandroidDatabase : RoomDatabase() {
    abstract fun channelDao(): ChannelDao
}
