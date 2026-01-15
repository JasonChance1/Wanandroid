package com.example.common.entities.state

sealed interface UiState<out T> {
    data object Idle : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T?) : UiState<T?>
    data class Error(val message: String, val code: Int? = null, val cause: Throwable? = null) : UiState<Nothing>
}
