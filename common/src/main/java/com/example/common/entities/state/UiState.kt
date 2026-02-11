package com.example.common.entities.state

sealed interface UiState {
    data object Idle : UiState
    data object Loading : UiState
    data object Success : UiState
    data object Empty : UiState
    data class Error(val message: String, val cause: Throwable? = null) : UiState
}
