package com.example.app_mvvm_kotlin.page.search

import com.example.app_mvvm_kotlin.App
import com.example.common.extensions.safeApiCall
import com.example.common.extensions.safeApiCallList
import com.example.common.net.CoreRetrofit
import com.example.common.net.service.SearchService
import com.example.model.ApiResult
import com.example.model.HotKey
import com.example.model.db.dao.HotKeyDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * @author wandervogel
 * @date 2026-02-12  星期四
 * @description
 */
class SearchRepository(
    val service: SearchService = CoreRetrofit.create(SearchService::class.java)
) {
    private val dao =App.instance.db.hotKeyDao()
    private val ttlMillis = 6 * 60 * 60 * 1000L // 6小时更新一次

    fun observeHotKeys(): Flow<List<HotKey>> =
        dao.observeHotKeys().map { list -> list.map { it.toModel() } }

    suspend fun hotKeys() = safeApiCallList { service.hotKeys() }

    suspend fun refreshIfNeed(force: Boolean = false): ApiResult<*> = withContext(Dispatchers.IO) {
        val last = dao.lastUpdatedAt() ?: 0L
        val now = System.currentTimeMillis()

        val needRefresh = force || last == 0L || (now - last) > ttlMillis
        if (!needRefresh) return@withContext ApiResult.Success(Unit)
        val r = service.hotKeys()
        if (r.isSuccess()) {
            val data = r.data.orEmpty()
            val updatedAt = System.currentTimeMillis()
            val entities = data.map { hk -> hk.toEntity(updatedAt) }
            dao.replaceAll(entities)
            ApiResult.Success(Unit)
        } else {
            ApiResult.Error("")
        }
    }
}