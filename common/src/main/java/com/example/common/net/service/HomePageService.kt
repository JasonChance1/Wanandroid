package com.example.common.net.service

import com.example.model.Article
import com.example.model.ArticleList
import com.example.model.BannerBean
import com.example.model.BaseResponse
import com.example.model.HotKey
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author wandervogel
 * @date 2026-01-14  星期三
 * @description
 */
interface HomePageService {

    @GET("banner/json")
    suspend fun getBanner(): BaseResponse<List<BannerBean>>

    @GET("article/top/json")
    suspend fun getTopArticle(): BaseResponse<List<Article>>

    @GET("article/list/{a}/json")
    suspend fun getArticles(
        @Path("a") a: Int
    ): BaseResponse<ArticleList>

    @GET("hotkey/json")
    suspend fun getHotKey(): BaseResponse<List<HotKey>>

    @POST("article/query/{page}/json")
    suspend fun getQueryArticleList(
        @Path("page") page: Int,
        @Query("k") k: String
    ): BaseResponse<ArticleList>

}