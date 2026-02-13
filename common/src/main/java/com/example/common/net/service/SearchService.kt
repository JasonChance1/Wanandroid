package com.example.common.net.service

import com.example.model.Article
import com.example.model.BaseResponse
import com.example.model.HotKey
import com.example.model.PageData
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchService {
    @GET("hotkey/json")
    suspend fun hotKeys():BaseResponse<List<HotKey>>

    @POST("article/query/{page}/json")
    suspend fun search(
        @Path("page") page: Int,
        @Query("k") k: String
    ): BaseResponse<PageData<Article>>
}