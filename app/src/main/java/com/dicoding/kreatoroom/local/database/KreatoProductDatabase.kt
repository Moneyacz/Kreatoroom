package com.dicoding.kreatoroom.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.kreatoroom.local.dao.KreatoProductDao
import com.dicoding.kreatoroom.local.entity.Entity

@Database(
    entities = [Entity::class],
    version = 1,
    exportSchema = false
)
abstract class KreatoProductDatabase: RoomDatabase() {

    abstract fun dao(): KreatoProductDao

    companion object {
        @Volatile
        private var INSTANCE: KreatoProductDatabase? = null

        fun getInstance(context: Context): KreatoProductDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    KreatoProductDatabase::class.java,
                "app_database"
                ).fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }

        }
    }

}