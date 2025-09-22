package com.example.common.net.bean

data class BaseResponse<T>(
    val data: T?,
    val errorCode: Int,
    val errorMsg: String
)