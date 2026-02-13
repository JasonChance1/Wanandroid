package com.example.model.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.HotKey

@Entity(tableName = "hot_key")
data class HotKeyEntity(
    val id: Int,
    val link: String = "",
    @PrimaryKey val name: String,
    val order: Int = 0,
    val visible: Int = 0,
    val updatedAt: Long
){
    fun toModel():HotKey{
        return HotKey(id,link, name, order, visible)
    }
}