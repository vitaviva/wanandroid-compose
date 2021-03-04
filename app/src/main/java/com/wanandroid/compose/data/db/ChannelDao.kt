package com.wanandroid.compose.data.db

import androidx.room.*
import com.wanandroid.compose.data.bean.ChildrenBean

@Dao
abstract class ChannelDao {
    @Query("SELECT * FROM channels")
    abstract suspend fun getChannels(): List<ChildrenBean>

    @Query("SELECT * FROM channels WHERE id = :id")
    abstract suspend fun getChannel(id: Int): ChildrenBean?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(entities: Collection<ChildrenBean>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entiry: ChildrenBean)

    @Delete
    abstract suspend fun delete(entiry: ChildrenBean): Int
}