package com.example.common.net.service

import com.example.model.ArticleList
import com.example.model.BaseResponse
import com.example.model.ProjectClassify
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProjectService {
    @GET("project/tree/json")
    suspend fun getProjectTree(): BaseResponse<List<ProjectClassify>>

    @GET("project/list/{page}/json")
    suspend fun getProject(@Path("page") page: Int, @Query("cid") cid: Int): BaseResponse<ArticleList>

}