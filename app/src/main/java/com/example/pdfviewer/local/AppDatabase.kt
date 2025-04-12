package com.example.pdfviewer.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ServiceItemEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun serviceDao(): ServiceItemDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "service_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}