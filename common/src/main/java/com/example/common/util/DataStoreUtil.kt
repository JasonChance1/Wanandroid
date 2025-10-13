package com.example.common.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException

// 创建 DataStore 扩展
private const val USER_PREFERENCES_NAME = "user_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)
/**
 * @author wandervogel
 * @date 2025-09-23  星期二
 * @description dataStore封装
 */
object DataStoreUtil {

    private lateinit var dataStore: DataStore<Preferences>

    /**
     * 初始化，在 Application.onCreate 调用一次
     */
    fun init(context: Context): DataStoreUtil {
        dataStore = context.dataStore
        return this
    }
    fun <T> getSync(key: String, default: T): T {
        return runBlocking {
            getOnce(key, default)
        }
    }

    fun <T> putSync(key: String, value: T) {
        runBlocking {
            putData(key, value)
        }
    }

    /**
     * 获取 Flow 数据
     */
    fun <T> getData(key: String, default: T): Flow<T> {
        val prefKey = keyOf(default, key)
        return dataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }.map { prefs ->
                prefs[prefKey] ?: default
            }
    }

    /**
     * 获取一次数据 (suspend)
     */
    suspend fun <T> getOnce(key: String, default: T): T {
        val prefKey = keyOf(default, key)
        return dataStore.data.map { prefs ->
            prefs[prefKey] ?: default
        }.first()
    }

    /**
     * 保存数据
     */
    suspend fun <T> putData(key: String, value: T) {
        val prefKey = keyOf(value, key)
        dataStore.edit { prefs ->
            prefs[prefKey] = value
        }
    }

    /**
     * 清空所有数据
     */
    suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    /**
     * 根据类型返回对应 Preferences.Key
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> keyOf(sample: T, name: String): Preferences.Key<T> =
        when (sample) {
            is Int -> intPreferencesKey(name)
            is Long -> longPreferencesKey(name)
            is Float -> floatPreferencesKey(name)
            is Boolean -> booleanPreferencesKey(name)
            is String -> stringPreferencesKey(name)
            else -> throw IllegalArgumentException("Unsupported type")
        } as Preferences.Key<T>
}
