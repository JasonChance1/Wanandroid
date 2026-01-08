package com.example.common.entities.event

sealed class LoginEvent {
    data class Toast(val msg: String) : LoginEvent()
    object LoginSuccess : LoginEvent()
    object GoRegister : LoginEvent()
}