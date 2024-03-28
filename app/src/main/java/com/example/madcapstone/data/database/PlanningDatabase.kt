package com.example.madcapstone.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.madcapstone.data.database.dao.TripDao
import com.example.madcapstone.data.database.dao.UserDao
import com.example.madcapstone.data.models.roomModels.SyncedUser
import com.example.madcapstone.data.models.roomModels.Trip

@Database(entities = [Trip::class, SyncedUser::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class PlanningDatabase: RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "PLANNING_DATABASE"

        // For Singleton instantiation
        @Volatile
        private var INSTANCE: PlanningDatabase? = null

        fun getDatabase(context: Context): PlanningDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlanningDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}