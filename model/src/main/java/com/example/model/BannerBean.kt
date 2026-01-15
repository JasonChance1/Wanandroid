package com.example.model

data class BannerBean(
    val uid: Int = 0,
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String,
    var filePath: String
)