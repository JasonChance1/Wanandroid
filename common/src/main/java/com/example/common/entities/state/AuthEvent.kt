package com.example.common.entities.state

sealed class AuthEvent {
    data class Toast(val msg: String) : AuthEvent()
    object LoginSuccess : AuthEvent()
    object RegisterSuccess : AuthEvent()
}
