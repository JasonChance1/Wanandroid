package com.example.app_mvvm_kotlin.page.collect

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.app_mvvm_kotlin.base.BaseViewModel
import com.example.app_mvvm_kotlin.paging.ArticlesPagingSource
import com.example.app_mvvm_kotlin.paging.CollectPagingSource
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
    companion object{
        const val CANCEL_SUCCESS = "cancel_success"
        const val COLLECT_SUCCESS = "collect_success"
    }
    private val repo = CollectRepository()

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
                    _events.emit(CANCEL_SUCCESS)
                }

                is ApiResult.Error -> _events.emit(r.message)
            }
        }
    }
    fun removeCollect(id: Int) {
        viewModelScope.launch {
            when (val r = repo.cancelCollect(id)) {
                is ApiResult.Success -> {

                }
                is ApiResult.Error -> {
                    // toast / event
                }
            }
        }
    }
    val collectList = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { CollectPagingSource(repo, _state) }
    ).flow.cachedIn(viewModelScope)
}
