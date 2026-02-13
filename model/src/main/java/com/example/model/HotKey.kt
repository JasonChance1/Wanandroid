package com.example.model

import com.example.model.db.entity.HotKeyEntity

data class HotKey(
    val id: Int,
    val link: String = "",
    val name: String,
    val order: Int = 0,
    val visible: Int = 0
) {
    fun toEntity(updatedAt: Long = System.currentTimeMillis()): HotKeyEntity {
        return HotKeyEntity(id, link, name, order, visible, updatedAt)
    }
}