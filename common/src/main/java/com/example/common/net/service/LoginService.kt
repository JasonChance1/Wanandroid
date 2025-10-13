package com.example.common.net.service
import com.example.model.BaseResponse
import com.example.model.Login
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {
    @POST("user/login")
    suspend fun getLogin(
        @Query("username") username: String,
        @Query("password") password: String
    ): BaseResponse<Login>

    @POST("user/register")
    suspend fun getRegister(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("repassword") repassword: String
    ): BaseResponse<Login>

    @GET("user/logout/json")
    suspend fun getLogout(): BaseResponse<Any>

}