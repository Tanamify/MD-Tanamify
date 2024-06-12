package com.bangkit.tanamify.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1)
abstract class TanamifyDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: TanamifyDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): TanamifyDatabase {
            if (INSTANCE == null) {
                synchronized(TanamifyDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        TanamifyDatabase::class.java, "tanamify_database"
                    )
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE as TanamifyDatabase
        }
    }
}