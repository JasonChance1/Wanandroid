package com.example.model

data class BaseResponse<T>(
    val data: T?,
    val errorCode: Int,
    val errorMsg: String
){
    fun isSuccess() = errorCode == 0
}