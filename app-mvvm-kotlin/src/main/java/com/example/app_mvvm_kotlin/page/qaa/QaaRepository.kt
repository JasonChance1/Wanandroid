package com.example.app_mvvm_kotlin.page.qaa

import com.example.common.extensions.safeApiCall
import com.example.common.net.CoreRetrofit
import com.example.common.net.service.QaaService

/**
 * @author wandervogel
 * @date 2026-02-10  星期二
 * @description
 */
class QaaRepository(private val service: QaaService = CoreRetrofit.create(QaaService::class.java)) {
    suspend fun getQaaList(page: Int) = safeApiCall { service.getQaaList(page) }
}