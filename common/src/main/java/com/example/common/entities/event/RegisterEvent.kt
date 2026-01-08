package com.example.common.entities.event

sealed class RegisterEvent {
    data class Toast(val msg: String) : RegisterEvent()
    data class RegisterSuccess(val username: String) : RegisterEvent()
}