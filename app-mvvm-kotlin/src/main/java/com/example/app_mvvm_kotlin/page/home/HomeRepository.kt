package com.example.app_mvvm_kotlin.page.home

import com.example.common.extensions.safeApiCall
import com.example.common.net.CoreRetrofit
import com.example.common.net.service.CollectService
import com.example.common.net.service.HomePageService

/**
 * @author wandervogel
 * @date 2026-01-15  星期四
 * @description
 */
class HomeRepository(
    private val service: HomePageService = CoreRetrofit.create(HomePageService::class.java),
    private val collectService: CollectService = CoreRetrofit.create(CollectService::class.java),
) {
    suspend fun getBanner() = safeApiCall { service.getBanner() }

    suspend fun getArticles(page:Int) = safeApiCall { service.getArticles(page) }

    suspend fun collect(id:Int) = safeApiCall { collectService.toCollect(id) }
    suspend fun cancelCollect(id:Int) = safeApiCall { collectService.cancelCollect(id) }
}