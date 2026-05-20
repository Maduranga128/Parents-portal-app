package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ParentEntity::class], version = 1, exportSchema = false)
abstract class ParentDatabase : RoomDatabase() {
    abstract val parentDao: ParentDao

    companion object {
        @Volatile
        private var INSTANCE: ParentDatabase? = null

        fun getDatabase(context: Context): ParentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ParentDatabase::class.java,
                    "parent_portal_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
