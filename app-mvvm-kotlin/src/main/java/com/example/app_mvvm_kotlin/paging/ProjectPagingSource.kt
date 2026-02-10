package com.example.app_mvvm_kotlin.paging

import com.example.app_mvvm_kotlin.page.project.ProjectRepository
import com.example.common.entities.state.UiState
import com.example.model.ApiResult
import com.example.model.Article
import com.example.model.ArticleList
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author wandervogel
 * @date 2026-02-10  星期二
 * @description 项目分页加载
 */
class ProjectPagingSource(
    private val repository: ProjectRepository,
    private val cid: Int,
    val state: MutableStateFlow<UiState>
) : BasePagingSource<Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 0

        return try {
            if (page == 0) {
                state.value = UiState.Loading
            }
            when (val r = repository.getProject(page, cid)) {
                is ApiResult.Success<*> -> {
                    val prevKey = if (page == 0) null else page - 1
                    if (page == 0) {
                        state.value = UiState.Success
                    }
                    (r.data as? ArticleList)?.let { data ->
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