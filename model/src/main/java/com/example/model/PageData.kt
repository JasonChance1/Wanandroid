package com.example.model

data class PageData<T>(  val offset: Int,
                         val over: Boolean,
                         val pageCount: Int,
                         val size: Int,
                         val total: Int,
                         val datas: List<T>,
                         val curPage: Int)
