package com.example.common.entities.state

import com.example.model.Login

data class AuthUiState(
    val username: String = "",
    val password: String = "",
    val repassword: String = "",
    val loading: Boolean = false,
    val user: Login? = null,
    val error: String? = null
)
