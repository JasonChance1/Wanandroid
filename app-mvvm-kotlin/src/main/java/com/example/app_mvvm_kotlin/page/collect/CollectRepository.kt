package com.example.app_mvvm_kotlin.page.collect

import com.example.common.extensions.safeApiCall
import com.example.common.net.CoreRetrofit
import com.example.common.net.service.CollectService

/**
 * @author wandervogel
 * @date 2026-02-10  星期二
 * @description
 */
class CollectRepository (private val collectService: CollectService = CoreRetrofit.create(
    CollectService::class.java)){

    suspend fun collect(id:Int) = safeApiCall { collectService.toCollect(id) }
    suspend fun cancelCollect(id:Int) = safeApiCall { collectService.cancelCollect(id) }
    suspend fun uncollect(id:Int) = safeApiCall { collectService.uncollect(id) }
    suspend fun getCollectList(page:Int) = safeApiCall { collectService.getCollectList(page) }
}