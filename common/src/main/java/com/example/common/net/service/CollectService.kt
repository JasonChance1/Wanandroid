package com.example.common.net.service

import com.example.model.BaseResponse
import com.example.model.CollectList
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CollectService {
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectList(@Path("page") page: Int): BaseResponse<CollectList>

    @POST("lg/collect/{id}/json")
    suspend fun toCollect(@Path("id") id: Int): BaseResponse<Any>

    @POST("lg/uncollect_originId/{id}/json")
    suspend fun cancelCollect(@Path("id") id: Int): BaseResponse<Any>
    @POST("lg/uncollect/{id}/json")
    suspend fun uncollect(@Path("id") id: Int): BaseResponse<Any>
}