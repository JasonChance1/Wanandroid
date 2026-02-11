package com.example.app_mvvm_kotlin.paging

import com.example.app_mvvm_kotlin.page.collect.CollectRepository
import com.example.app_mvvm_kotlin.page.qaa.QaaRepository
import com.example.common.entities.state.UiState
import com.example.model.ApiResult
import com.example.model.Article
import com.example.model.ArticleList
import com.example.model.Collect
import com.example.model.CollectList
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author wandervogel
 * @date 2026-02-10  星期二
 * @description
 */
class CollectPagingSource(private val repository: CollectRepository, val state: MutableStateFlow<UiState>) :
    BasePagingSource<Collect>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Collect> {
        val page = params.key ?: 0
        return try {
            if (page == 0) {
                state.value = UiState.Loading
            }
            when (val r = repository.getCollectList(page)) {
                is ApiResult.Success<*> -> {
                    val prevKey = if (page == 0) null else page - 1
                    if (page == 0) {
                        state.value = UiState.Success
                    }
                    (r.data as? CollectList)?.let { data ->
                        try {
                            val nextKey = if (data.over) null else page + 1

                            LoadResult.Page(
                                data = data.datas,
                                prevKey = prevKey,
                                nextKey = nextKey
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                            LoadResult.Page(
                                data = emptyList(),
                                prevKey,
                                null
                            )
                        }
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