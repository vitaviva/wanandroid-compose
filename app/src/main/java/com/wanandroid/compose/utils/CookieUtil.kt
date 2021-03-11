package com.wanandroid.compose.utils

import androidx.datastore.preferences.core.stringPreferencesKey
import com.wanandroid.compose.data.sp.WanandroidDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


internal fun readCookie(domain: String?): String {
    var res = ""
    runBlocking {
        domain ?: return@runBlocking
        res = WanandroidDataStore.getValueFlow(stringPreferencesKey(domain), "")
            .first()
    }
    return res
}

internal fun saveCookie(url: String?, domain: String?, cookies: String) {
    runBlocking {
        url ?: return@runBlocking
        WanandroidDataStore.setValue(stringPreferencesKey(url), cookies)
        domain ?: return@runBlocking
        WanandroidDataStore.setValue(stringPreferencesKey(domain), cookies)
    }

}

internal fun clearCookie(): Boolean {
    runBlocking {
        try {
            WanandroidDataStore.clear()
        } catch (ignore: Exception) {
            return@runBlocking false
        }
    }
    return true
}

/**
 * save cookie string
 */
internal fun encodeCookie(cookies: List<String>): String {
    val sb = StringBuilder()
    val set = HashSet<String>()
    cookies
        .map { cookie ->
            cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        }
        .forEach { it ->
            it.filterNot { set.contains(it) }.forEach { set.add(it) }
        }

    val ite = set.iterator()
    while (ite.hasNext()) {
        val cookie = ite.next()
        sb.append(cookie).append(";")
    }

    val last = sb.lastIndexOf(";")
    if (sb.length - 1 == last) {
        sb.deleteCharAt(last)
    }

    return sb.toString()
}
