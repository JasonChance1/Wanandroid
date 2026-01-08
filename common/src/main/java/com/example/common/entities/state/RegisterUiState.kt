package com.example.common.entities.state

data class RegisterUiState(
    val username: String = "",
    val password: String = "",
    val repassword: String = "",
    val loading: Boolean = false,
    val error: String? = null
)