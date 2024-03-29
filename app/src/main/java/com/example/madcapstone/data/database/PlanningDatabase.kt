package com.example.madcapstone.data.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.madcapstone.data.database.dao.RoomActivityDao
import com.example.madcapstone.data.database.dao.TripActivityDao
import com.example.madcapstone.data.database.dao.TripDao
import com.example.madcapstone.data.database.dao.UserDao
import com.example.madcapstone.data.models.roomModels.RoomActivity
import com.example.madcapstone.data.models.roomModels.SyncedUser
import com.example.madcapstone.data.models.roomModels.Trip
import com.example.madcapstone.data.models.roomModels.TripActivity

@Database(entities = [Trip::class, SyncedUser::class, TripActivity::class, RoomActivity::class], version = 2, exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
    )
@TypeConverters(TypeConverter::class)
abstract class PlanningDatabase: RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun userDao(): UserDao

    abstract fun roomActivityDao(): RoomActivityDao
    abstract fun tripActivityDao(): TripActivityDao

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