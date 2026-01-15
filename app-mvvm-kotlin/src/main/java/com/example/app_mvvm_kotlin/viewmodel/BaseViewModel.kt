package com.example.app_mvvm_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import com.example.common.entities.state.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author wandervogel
 * @date 2026-01-15  星期四
 * @description
 */
open class BaseViewModel : ViewModel() {
    // 一次性事件（Toast、SnackBar、导航）
    protected val _events = MutableSharedFlow<String>()
    val events = _events


    protected val _state = MutableStateFlow<UiState<*>>(
        UiState.Idle
    )
    val state: StateFlow<UiState<*>> = _state
}