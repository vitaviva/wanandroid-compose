package com.wanandroid.compose.data.sp

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


internal val KEY_ACCOUNT = stringSetPreferencesKey("loginInfo")

@SuppressLint("StaticFieldLeak")
object WanandroidDataStore {

    private lateinit var _context: Context

    fun init(context: Context) {
        _context = context
    }

    internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "account")

    fun <T> getValueFlow(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> {
        return _context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[key] ?: defaultValue
            }
    }

    suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        _context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}
