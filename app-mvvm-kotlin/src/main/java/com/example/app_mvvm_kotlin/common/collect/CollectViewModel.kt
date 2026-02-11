package com.example.app_mvvm_kotlin.common.collect

import androidx.lifecycle.viewModelScope
import com.example.app_mvvm_kotlin.base.BaseViewModel
import com.example.app_mvvm_kotlin.common.collect.CollectRepository
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
                is ApiResult.Success -> setCollect(id, false)
                is ApiResult.Error -> _events.emit(r.message)
            }
        }
    }
}
