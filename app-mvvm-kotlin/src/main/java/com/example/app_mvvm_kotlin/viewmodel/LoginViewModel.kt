package com.example.app_mvvm_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_mvvm_kotlin.repositories.AuthRepository
import com.example.common.constant.DSConstant
import com.example.common.entities.event.LoginEvent
import com.example.common.entities.state.LoginUiState
import com.example.common.util.DataStoreUtil
import com.example.model.ApiResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _event = Channel<LoginEvent>(Channel.BUFFERED)
    val eventFlow = _event.receiveAsFlow()

    fun onUsernameChanged(v: String) = _uiState.update { it.copy(username = v, error = null) }
    fun onPasswordChanged(v: String) = _uiState.update { it.copy(password = v, error = null) }

    fun clickRegister() {
        viewModelScope.launch { _event.send(LoginEvent.GoRegister) }
    }

    init {
        viewModelScope.launch {
            // 预填账号
            DataStoreUtil.getData(DSConstant.USERNAME, "").collect { username ->
                _uiState.update { it.copy(username = username) }
            }
        }
    }

    fun login() {
        val s = _uiState.value
        val username = s.username.trim()
        val password = s.password

        if (username.isBlank() || password.isBlank()) {
            viewModelScope.launch { _event.send(LoginEvent.Toast("请输入账号和密码")) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            when (val r = repo.login(username, password)) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(loading = false) }
                    _event.send(LoginEvent.LoginSuccess)
                }

                is ApiResult.Error -> {
                    _uiState.update { it.copy(loading = false, error = r.message) }
                    _event.send(LoginEvent.Toast(r.message))
                }
            }
        }
    }

    fun applyRegisteredUsername(username: String) {
        if (username.isNotBlank()) _uiState.update { it.copy(username = username) }
    }
}
