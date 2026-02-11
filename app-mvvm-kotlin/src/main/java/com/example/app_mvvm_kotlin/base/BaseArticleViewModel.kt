package com.example.app_mvvm_kotlin.base

import androidx.lifecycle.viewModelScope
import com.example.app_mvvm_kotlin.page.collect.CollectRepository
import com.example.model.ApiResult
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-02-10  星期二
 * @description
 */
open class BaseArticleViewModel :BaseViewModel() {
    private val collectRepository: CollectRepository = CollectRepository()
    fun collect(id: Int) {
        viewModelScope.launch {
            when (val r = collectRepository.collect(id)) {
                is ApiResult.Success -> {}
                is ApiResult.Error -> {
                    _events.emit(r.message)
                }
            }
        }
    }
    fun cancelCollect(id: Int) {
        viewModelScope.launch {
            when (val r = collectRepository.cancelCollect(id)) {
                is ApiResult.Success -> {}
                is ApiResult.Error -> {
                    _events.emit(r.message)
                }
            }
        }
    }
}