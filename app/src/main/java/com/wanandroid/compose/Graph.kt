
package com.wanandroid.compose

import android.content.Context
import androidx.room.Room
import com.wanandroid.compose.data.db.WanandroidDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File

/**
 * A very simple global singleton dependency graph.
 *
 * For a real app, you would use something like Hilt/Dagger instead.
 */
object Graph {
    lateinit var okHttpClient: OkHttpClient

    lateinit var database: WanandroidDatabase
        private set

    internal val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    internal val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO

    fun provide(context: Context) {
        okHttpClient = OkHttpClient.Builder()
            .cache(Cache(File(context.cacheDir, "http_cache"), 20 * 1024 * 1024))
            .build()

        database = Room.databaseBuilder(context, WanandroidDatabase::class.java, "data.db")
            // This is not recommended for normal apps, but the goal of this sample isn't to
            // showcase all of Room.
            .fallbackToDestructiveMigration()
            .build()
    }
}
