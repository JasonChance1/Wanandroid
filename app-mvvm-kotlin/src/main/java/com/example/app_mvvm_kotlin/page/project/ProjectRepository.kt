package com.example.app_mvvm_kotlin.page.project

import com.example.common.extensions.safeApiCall
import com.example.common.net.CoreRetrofit
import com.example.common.net.service.ProjectService

/**
 * @author wandervogel
 * @date 2026-02-10  星期二
 * @description
 */
class ProjectRepository(private val service: ProjectService = CoreRetrofit.create(ProjectService::class.java)) {
    suspend fun getProjectTree() = safeApiCall { service.getProjectTree() }

    suspend fun getProject(page: Int, cid: Int) = safeApiCall { service.getProject(page, cid) }
}