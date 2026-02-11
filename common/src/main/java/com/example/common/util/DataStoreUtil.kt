package com.example.common.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.common.constant.DSConstant
import com.example.common.net.CookieCodec
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Cookie
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

/**
 * @author wandervogel
 * @date 2025-09-23  星期二
 * @description dataStore封装
 */
object DataStoreUtil {

    private lateinit var dataStore: DataStore<Preferences>
    lateinit var gson: Gson

    /**
     * 初始化，在 Application.onCreate 调用一次
     */
    fun init(context: Context): DataStoreUtil {
        dataStore = context.dataStore
        gson = Gson()
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

    suspend fun loadCookies(): List<Cookie> {
        val set = dataStore.data.first()[DSConstant.COOKIES].orEmpty()
        val now = System.currentTimeMillis()
        return set.mapNotNull { CookieCodec.decode(it) }
            .filter { it.expiresAt > now }
    }

    suspend fun saveCookies(cookies: List<Cookie>) {
        val now = System.currentTimeMillis()
        val set = cookies
            .filter { it.expiresAt > now }
            .map { CookieCodec.encode(it) }
            .toSet()

        dataStore.edit { prefs ->
            prefs[DSConstant.COOKIES] = set
        }
    }

    suspend fun <T> putObject(key: String, value: T) {
        putData(key, gson.toJson(value))
    }

    suspend inline fun <reified T> getObjectOnce(key: String, default: T? = null): T? {
        val json = getOnce(key, "")
        if (json.isBlank()) return default
        return runCatching { gson.fromJson(json, T::class.java) }.getOrElse { default }
    }

    fun <T> getObjectFlow(key: String, clazz: Class<T>, default: T? = null): Flow<T?> {
        return getData(key, "").map { json ->
            if (json.isBlank()) default
            else runCatching { gson.fromJson(json, clazz) }.getOrElse { default }
        }
    }

    /**
     * 根据类型返回对应 Preferences.Key
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> keyOf(sample: T?, name: String): Preferences.Key<T> =
        when (sample) {
            is Int -> intPreferencesKey(name)
            is Long -> longPreferencesKey(name)
            is Float -> floatPreferencesKey(name)
            is Boolean -> booleanPreferencesKey(name)
            is String -> stringPreferencesKey(name)
            else -> throw IllegalArgumentException(if (sample == null) "value cannot be null" else "Unsupported type:${sample!!::class.simpleName}")
        } as Preferences.Key<T>
}
