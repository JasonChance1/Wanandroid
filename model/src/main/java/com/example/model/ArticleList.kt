package com.example.model

data class ArticleList(
    /**
     * 当前页
     */
    val curPage: Int,
    /**
     * 当前页的数据
     */
    val datas: List<Article>,
    val offset: Int,
    val over: Boolean,
    /**
     * 总页数
     */
    val pageCount: Int,
    /**
     * 当前页的数据条数
     */
    val size: Int,
    /**
     * 总数据条数
     */
    val total: Int
)