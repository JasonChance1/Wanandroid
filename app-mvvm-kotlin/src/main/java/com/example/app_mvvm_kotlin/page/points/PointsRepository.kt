package com.example.app_mvvm_kotlin.page.points

import com.example.common.extensions.safeApiCall
import com.example.common.net.CoreRetrofit
import com.example.common.net.service.PointsService

/**
 * @author wandervogel
 * @date 2026-02-11  星期三
 * @description
 */
class PointsRepository(private val service: PointsService = CoreRetrofit.create(PointsService::class.java)) {
    suspend fun getPointList(page:Int) = safeApiCall { service.getProjectTree(page) }
    suspend fun getRank(page:Int) = safeApiCall { service.getRank(page) }
    suspend fun getMyRank() = safeApiCall { service.getMyRank() }
}