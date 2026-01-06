package com.example.app_mvvm_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_mvvm_kotlin.repositories.AuthRepository
import com.example.common.entities.state.AuthEvent
import com.example.common.entities.state.AuthUiState
import com.example.model.ApiResult
import com.example.model.Login
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _event = Channel<AuthEvent>(Channel.BUFFERED)
    val eventFlow = _event.receiveAsFlow()

    fun onUsernameChanged(v: String) = _uiState.update { it.copy(username = v, error = null) }
    fun onPasswordChanged(v: String) = _uiState.update { it.copy(password = v, error = null) }
    fun onRepasswordChanged(v: String) = _uiState.update { it.copy(repassword = v, error = null) }

    fun login() {
        val s = _uiState.value
        if (s.username.isBlank() || s.password.isBlank()) {
            viewModelScope.launch { _event.send(AuthEvent.Toast("请输入账号和密码")) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            when (val r = repo.login(s.username.trim(), s.password)) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(loading = false, user = r.data) }
                    _event.send(AuthEvent.LoginSuccess)
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(loading = false, error = r.message) }
                    _event.send(AuthEvent.Toast(r.message))
                }
            }
        }
    }

    fun register() {
        val s = _uiState.value
        if (s.username.isBlank() || s.password.isBlank() || s.repassword.isBlank()) {
            viewModelScope.launch { _event.send(AuthEvent.Toast("请完整填写注册信息")) }
            return
        }
        if (s.password != s.repassword) {
            viewModelScope.launch { _event.send(AuthEvent.Toast("两次密码不一致")) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            when (val r = repo.register(s.username.trim(), s.password, s.repassword)) {
                is ApiResult.Success<Login> -> {
                    _uiState.update { it.copy(loading = false, user = r.data) }
                    _event.send(AuthEvent.RegisterSuccess)
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(loading = false, error = r.message) }
                    _event.send(AuthEvent.Toast(r.message))
                }
            }
        }
    }
}
