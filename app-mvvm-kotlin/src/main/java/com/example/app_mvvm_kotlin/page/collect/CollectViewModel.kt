package com.example.app_mvvm_kotlin.page.collect

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.app_mvvm_kotlin.paging.CollectPagingSource
import com.example.common.ui.BaseViewModel
import com.example.model.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-02-10  星期二
 * @description
 */
open class CollectViewModel : BaseViewModel() {

    private val repo = CollectRepository()
    private var currentPagingSource: CollectPagingSource? = null
    private val _collectOverrides = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val collectOverrides = _collectOverrides.asStateFlow()

    private fun setCollect(id: Int, collect: Boolean) {
        _collectOverrides.update { it + (id to collect) }
    }

    fun collect(id: Int) {
        viewModelScope.launch {
            when (val r = repo.collect(id)) {
                is ApiResult.Success -> setCollect(id, true)
                is ApiResult.Error -> _events.emit(r.message)
            }
        }
    }

    fun cancelCollect(id: Int) {
        viewModelScope.launch {
            when (val r = repo.cancelCollect(id)) {
                is ApiResult.Success -> {
                    setCollect(id, false)
                }

                is ApiResult.Error -> _events.emit(r.message)
            }
        }
    }

    val collectList = Pager(
        PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            CollectPagingSource(repo,_state).also { currentPagingSource = it }
        }
    ).flow.cachedIn(viewModelScope)

    fun removeCollect(id: Int) {
        viewModelScope.launch {
            when (val r = repo.cancelCollect(id)) {
                is ApiResult.Success -> currentPagingSource?.invalidate()
                is ApiResult.Error ->  _events.emit(r.message)
            }
        }
    }
}
