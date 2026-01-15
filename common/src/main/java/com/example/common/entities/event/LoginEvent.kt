package com.example.common.entities.event

import com.example.model.Login

sealed class LoginEvent {
    data class Toast(val msg: String) : LoginEvent()
    class LoginSuccess(val login:Login?) : LoginEvent()
    object GoRegister : LoginEvent()
}