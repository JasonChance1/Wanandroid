package com.example.app_mvvm_kotlin.paging

import com.example.app_mvvm_kotlin.page.points.PointsRepository
import com.example.app_mvvm_kotlin.page.qaa.QaaRepository
import com.example.common.entities.state.UiState
import com.example.model.ApiResult
import com.example.model.Article
import com.example.model.ArticleList
import com.example.model.PageData
import com.example.model.Points
import com.example.model.PointsList
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author wandervogel
 * @date 2026-02-10  星期二
 * @description
 */
class PointsPagingSource(private val repository: PointsRepository, val state: MutableStateFlow<UiState>) :
    BasePagingSource<Points>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Points> {
        val page = params.key ?: 0
        return try {
            if (page == 0) {
                state.value = UiState.Loading
            }
            when (val r = repository.getPointList(page)) {
                is ApiResult.Success<*> -> {
                    val prevKey = if (page == 0) null else page - 1
                    if (page == 0) {
                        state.value = UiState.Success
                    }
                    (r.data as? PointsList)?.let { data ->
                        val nextKey = if (data.over) null else page + 1

                        LoadResult.Page(
                            data = data.datas,
                            prevKey = prevKey,
                            nextKey = nextKey
                        )
                    } ?: LoadResult.Page(
                        data = emptyList(),
                        prevKey,
                        null
                    )
                }

                is ApiResult.Error -> {
                    if (page == 0) {
                        state.value = UiState.Error(r.message, r.throwable)
                    }
                    LoadResult.Error(r.throwable ?: RuntimeException(r.message))
                }
            }
        } catch (t: Throwable) {
            LoadResult.Error(t)
        }
    }
}