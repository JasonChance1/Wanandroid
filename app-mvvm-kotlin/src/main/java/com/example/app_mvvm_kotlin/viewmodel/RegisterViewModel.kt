package com.example.app_mvvm_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_mvvm_kotlin.repositories.AuthRepository
import com.example.common.entities.event.RegisterEvent
import com.example.common.entities.state.RegisterUiState
import com.example.model.ApiResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _event = Channel<RegisterEvent>(Channel.BUFFERED)
    val eventFlow = _event.receiveAsFlow()

    fun onUsernameChanged(v: String) = _uiState.update { it.copy(username = v, error = null) }
    fun onPasswordChanged(v: String) = _uiState.update { it.copy(password = v, error = null) }
    fun onRepasswordChanged(v: String) = _uiState.update { it.copy(repassword = v, error = null) }

    fun register() {
        val s = _uiState.value
        val username = s.username.trim()
        val password = s.password
        val repassword = s.repassword

        if (username.isBlank() || password.isBlank() || repassword.isBlank()) {
            viewModelScope.launch { _event.send(RegisterEvent.Toast("请完整填写注册信息")) }
            return
        }
        if (password != repassword) {
            viewModelScope.launch { _event.send(RegisterEvent.Toast("两次密码不一致")) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            when (val r = repo.register(username, password, repassword)) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(loading = false) }
                    _event.send(RegisterEvent.RegisterSuccess(username))
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(loading = false, error = r.message) }
                    _event.send(RegisterEvent.Toast(r.message))
                }
            }
        }
    }
}
