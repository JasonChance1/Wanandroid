package com.example.app_mvvm_kotlin.page.auth

import com.example.common.net.CoreRetrofit
import com.example.common.net.service.LoginService
import com.example.common.extensions.safeApiCall
import com.example.model.ApiResult
import com.example.model.Login

class AuthRepository(
    private val service: LoginService = CoreRetrofit.create(LoginService::class.java)
) {

    suspend fun login(username: String, password: String): ApiResult<Login?> =
        safeApiCall { service.login(username, password) }

    suspend fun register(username: String, password: String, repassword: String): ApiResult<Login?> =
        safeApiCall { service.register(username, password, repassword) }

    suspend fun logout(): ApiResult<Unit> =
        try {
            val resp = service.logout()
            if (resp.isSuccess()) ApiResult.Success(Unit)
            else ApiResult.Error(resp.errorMsg)
        } catch (t: Throwable) {
            ApiResult.Error(t.message ?: "Network error", t)
        }
}
