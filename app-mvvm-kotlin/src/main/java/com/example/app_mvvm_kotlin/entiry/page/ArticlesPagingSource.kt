package com.example.app_mvvm_kotlin.entiry.page

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.app_mvvm_kotlin.repositories.HomeRepository
import com.example.model.ApiResult
import com.example.model.Article
import com.example.model.ArticleList

class ArticlesPagingSource(
    private val repository: HomeRepository
) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchor ->
            val page = state.closestPageToPosition(anchor)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 0
        return try {
            when (val r = repository.getArticles(page)) {
                is ApiResult.Success<*> -> {
                    val prevKey = if (page == 0) null else page - 1
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
                    LoadResult.Error(r.throwable ?: RuntimeException(r.message))
                }
            }
        } catch (t: Throwable) {
            LoadResult.Error(t)
        }
    }
}
