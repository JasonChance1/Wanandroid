package com.example.common.net.service

import com.example.model.BaseResponse
import com.example.model.HotKey
import retrofit2.http.GET

interface SearchService {
    @GET("hotkey/json")
    suspend fun hotKeys():BaseResponse<List<HotKey>>
}