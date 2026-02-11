package com.example.common.net.service

import com.example.model.ArticleList
import com.example.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface QaaService {
    @GET("wenda/list/{pageId}/json")
    suspend fun getQaaList(@Path("pageId") page: Int): BaseResponse<ArticleList>
}