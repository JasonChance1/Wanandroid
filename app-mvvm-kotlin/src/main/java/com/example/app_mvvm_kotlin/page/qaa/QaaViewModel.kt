package com.example.app_mvvm_kotlin.page.qaa

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.app_mvvm_kotlin.paging.QaaPagingSource
import com.example.common.ui.BaseViewModel

/**
 * @author wandervogel
 * @date 2026-02-10  星期二
 * @description
 */
class QaaViewModel : BaseViewModel() {
    private val repository: QaaRepository = QaaRepository()

    val dataFlow = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { QaaPagingSource(repository, _state) }
    ).flow.cachedIn(viewModelScope)
    // 分页数据
//    private val pagerFlow = Pager(
//        config = PagingConfig(pageSize = 20, initialLoadSize = 40, enablePlaceholders = false),
//        pagingSourceFactory = { QaaPagingSource(repository, _state) }
//    ).flow
//    // 收藏、取消收藏之后合并，避免对应item重新显示时状态错误
//    val dataFlow = combine(pagerFlow, collectOverrides) { paging, map ->
//        paging.map { a ->
//            map[a.id]?.let { a.copy(collect = it) } ?: a
//        }
//    }.cachedIn(viewModelScope)
}