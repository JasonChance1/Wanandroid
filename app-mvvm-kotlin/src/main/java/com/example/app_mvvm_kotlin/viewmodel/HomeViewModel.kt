package com.example.app_mvvm_kotlin.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.app_mvvm_kotlin.repositories.HomeRepository
import com.example.common.entities.state.UiState
import com.example.model.ApiResult
import com.example.model.BannerBean
import kotlinx.coroutines.launch

/**
 * @author wandervogel
 * @date 2026-01-15  星期四
 * @description
 */
class HomeViewModel() : BaseViewModel() {
    private val repository: HomeRepository = HomeRepository()

    fun getBanner() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            when (val r = repository.getBanner()) {
                is ApiResult.Success -> _state.value = UiState.Success(r.data)
                is ApiResult.Error -> {
                    _state.value = UiState.Error(r.message, cause = r.throwable)
                    _events.emit(r.message)
                }
            }
        }
    }

    fun getArticles(page: Int = 0) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            when (val r = repository.getArticles(page)) {
                is ApiResult.Success -> _state.value = UiState.Success(r.data)
                is ApiResult.Error -> {
                    _state.value = UiState.Error(r.message, cause = r.throwable)
                    _events.emit(r.message)
                }
            }
        }
    }
}
