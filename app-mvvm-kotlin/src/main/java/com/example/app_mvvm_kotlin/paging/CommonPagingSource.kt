package com.example.app_mvvm_kotlin.paging

import com.example.common.entities.state.UiState
import com.example.model.ApiResult
import com.example.model.PageData
import kotlinx.coroutines.flow.MutableStateFlow

class CommonPagingSource<T : Any>(
    private val state: MutableStateFlow<UiState>,
    private val fetch: suspend (page: Int) -> ApiResult<*>,
    private val mapper: (Any?) -> PageData<T>?
) : BasePagingSource<T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 0
        return try {
            if (page == 0) state.value = UiState.Loading

            when (val r = fetch(page)) {
                is ApiResult.Success<*> -> {
                    val prevKey = if (page == 0) null else page - 1
                    if (page == 0) state.value = UiState.Success

                    val data = mapper(r.data)
                    if (data == null) {
                        LoadResult.Page(
                            data = emptyList(),
                            prevKey = prevKey,
                            nextKey = null
                        )
                    } else {
                        val nextKey = if (data.over) null else page + 1
                        LoadResult.Page(
                            data = data.datas,
                            prevKey = prevKey,
                            nextKey = nextKey
                        )
                    }
                }

                is ApiResult.Error -> {
                    if (page == 0) state.value = UiState.Error(r.message, r.throwable)
                    LoadResult.Error(r.throwable ?: RuntimeException(r.message))
                }
            }
        } catch (t: Throwable) {
            LoadResult.Error(t)
        }
    }
}
