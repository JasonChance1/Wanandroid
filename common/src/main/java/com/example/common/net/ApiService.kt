package com.example.common.net

import com.example.common.net.bean.BaseResponse
import com.example.common.net.bean.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

// ApiService.kt
interface ApiService {
    // 退出登录 GET
    @GET("user/logout/json")
    suspend fun logout(): BaseResponse<Any>

    // 注册 POST
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): BaseResponse<User>
}