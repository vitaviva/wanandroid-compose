package com.wanandroid.compose

import android.content.Context
import androidx.room.Room
import com.wanandroid.compose.data.db.WanandroidDatabase
import com.wanandroid.compose.data.sp.WanandroidDataStore
import com.wanandroid.compose.utils.encodeCookie
import com.wanandroid.compose.utils.readCookie
import com.wanandroid.compose.utils.saveCookie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * A very simple global singleton dependency graph.
 *
 * For a real app, you would use something like Hilt/Dagger instead.
 */
object Graph {

    private const val SAVE_USER_LOGIN_KEY = "user/login"
    private const val SAVE_USER_REGISTER_KEY = "user/register"
    private const val SET_COOKIE_KEY = "set-cookie"
    private const val COOKIE_NAME = "Cookie"
    private const val CONNECT_TIMEOUT = 30L
    private const val READ_TIMEOUT = 10L

    lateinit var okHttpClient: OkHttpClient

    lateinit var database: WanandroidDatabase
        private set

    internal val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    internal val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO

    fun provide(context: Context) {
        okHttpClient = OkHttpClient.Builder()
            .apply {
                connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                // get response cookie
                addInterceptor {
                    val request = it.request()
                    val response = it.proceed(request)
                    val requestUrl = request.url().toString()
                    val domain = request.url().host()
                    // set-cookie maybe has multi, login to save cookie
                    if ((requestUrl.contains(SAVE_USER_LOGIN_KEY) || requestUrl.contains(
                            SAVE_USER_REGISTER_KEY
                        ))
                        && response.headers(SET_COOKIE_KEY).isNotEmpty()
                    ) {
                        val cookies = response.headers(SET_COOKIE_KEY)
                        val cookie = encodeCookie(cookies)
                        saveCookie(requestUrl, domain, cookie)
                    }
                    response
                }
                // set request cookie
                addInterceptor {
                    val request = it.request()
                    val builder = request.newBuilder()
                    val domain = request.url().host()
                    // get domain cookie
                    if (domain.isNotEmpty()) {
                        val spDomain: String = readCookie(domain)
                        val cookie: String = if (spDomain.isNotEmpty()) spDomain else ""
                        if (cookie.isNotEmpty()) {
                            builder.addHeader(COOKIE_NAME, cookie)
                        }
                    }
                    it.proceed(builder.build())
                }
            }.build()

        database = Room.databaseBuilder(context, WanandroidDatabase::class.java, "data.db")
            // This is not recommended for normal apps, but the goal of this sample isn't to
            // showcase all of Room.
            .fallbackToDestructiveMigration()
            .build()


        WanandroidDataStore.init(context)
    }
}
