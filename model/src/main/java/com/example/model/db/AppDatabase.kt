package com.example.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.model.db.dao.HotKeyDao
import com.example.model.db.entity.HotKeyEntity

@Database(entities = [HotKeyEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun hotKeyDao(): HotKeyDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app.db"
                )
//                    .addMigrations(MIGRATION_1_2)
                    // .fallbackToDestructiveMigration() // 强制更新
                    .build()
                    .also { INSTANCE = it }
            }
        }

        //        /**
//         * v1 -> v2:
//         * - 新增搜索记录表 search_history
//         */
//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(db: SupportSQLiteDatabase) {
//
//                // 1) 新建表：search_history
//                // 注意点：
//                // - SQLite 没有 Long 类型，INTEGER 对应 Kotlin Long/Int
//                // - 主键自增：INTEGER PRIMARY KEY AUTOINCREMENT
//                // - NOT NULL 约束：保持与 Entity 一致
//                db.execSQL(
//                    """
//                    CREATE TABLE IF NOT EXISTS `search_history` (
//                        `hid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
//                        `keyword` TEXT NOT NULL,
//                        `lastUsedAt` INTEGER NOT NULL,
//                        `count` INTEGER NOT NULL
//                    )
//                    """.trimIndent()
//                )
//
//                // 2) 建索引：keyword 唯一（对应 Entity 的 indices = unique）
//                // SQLite 索引命名建议：index_<table>_<column>
//                db.execSQL(
//                    """
//                    CREATE UNIQUE INDEX IF NOT EXISTS `index_search_history_keyword`
//                    ON `search_history` (`keyword`)
//                    """.trimIndent()
//                )
//
//                // 如果你还想加一个普通索引（比如按 lastUsedAt 排序常用），也可以：
//                // db.execSQL("CREATE INDEX IF NOT EXISTS `index_search_history_lastUsedAt` ON `search_history` (`lastUsedAt`)")
//            }
//        }
    }
}
