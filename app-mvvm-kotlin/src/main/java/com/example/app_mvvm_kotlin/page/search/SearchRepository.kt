package com.example.app_mvvm_kotlin.page.search

import com.example.common.extensions.safeApiCallList
import com.example.common.net.CoreRetrofit
import com.example.common.net.service.SearchService

/**
 * @author wandervogel
 * @date 2026-02-12  星期四
 * @description
 */
class SearchRepository(val service: SearchService = CoreRetrofit.create(SearchService::class.java)) {
    suspend fun hotKeys() = safeApiCallList { service.hotKeys() }
}