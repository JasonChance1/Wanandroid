package com.example.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.model.db.entity.HotKeyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HotKeyDao {

    @Query("SELECT * FROM hot_key ORDER BY `order` ASC")
    fun observeHotKeys(): Flow<List<HotKeyEntity>>

    @Query("SELECT MAX(updatedAt) FROM hot_key")
    suspend fun lastUpdatedAt(): Long?

    @Query("DELETE FROM hot_key")
    suspend fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<HotKeyEntity>)

    @Transaction
    suspend fun replaceAll(list: List<HotKeyEntity>) {
        clear()
        insertAll(list)
    }
}
