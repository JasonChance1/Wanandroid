package com.example.common.net.service

import com.example.model.BaseResponse
import com.example.model.PageData
import com.example.model.Points
import com.example.model.PointsRank
import retrofit2.http.GET
import retrofit2.http.Path

interface PointsService {
    @GET("lg/coin/list/{page}/json")
    suspend fun getProjectTree(@Path("page") page:Int): BaseResponse<PageData<Points>>
    @GET("coin/rank/{page}/json")
    suspend fun getRank(@Path("page") page:Int): BaseResponse<PageData<PointsRank>>
    @GET("lg/coin/userinfo/json")
    suspend fun getMyRank(): BaseResponse<PointsRank>
}