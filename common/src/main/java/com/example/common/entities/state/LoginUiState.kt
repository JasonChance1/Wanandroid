package com.example.common.entities.state

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null
)