package com.example.app_mvvm_kotlin.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * @author wandervogel
 * @date 2026-02-10  星期二
 * @description
 */
abstract class BasePagingSource<T : Any> : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchor ->
            val page = state.closestPageToPosition(anchor)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }
}