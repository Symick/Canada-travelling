package com.example.madcapstone.data.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.madcapstone.data.database.dao.DeletedEntityDao
import com.example.madcapstone.data.database.dao.RoomActivityDao
import com.example.madcapstone.data.database.dao.TripActivityDao
import com.example.madcapstone.data.database.dao.TripDao
import com.example.madcapstone.data.database.dao.UserDao
import com.example.madcapstone.data.models.roomModels.DeletedEntity
import com.example.madcapstone.data.models.roomModels.RoomActivity
import com.example.madcapstone.data.models.roomModels.SyncedUser
import com.example.madcapstone.data.models.roomModels.Trip
import com.example.madcapstone.data.models.roomModels.TripActivity

/**
 * Database for the planning app.
 * This database contains the tables for the trips, activities, users and deleted entities.
 * The database is used to store the data locally.
 *
 * @property tripDao The Data Access Object for the Trip table.
 * @property userDao The Data Access Object for the User table.
 * @property roomActivityDao The Data Access Object for the RoomActivity table.
 * @property tripActivityDao The Data Access Object for the TripActivity table.
 * @property deletedEntityDao The Data Access Object for the DeletedEntity table.
 *
 *  @author Julian Kruithof
 */
@Database(entities = [Trip::class, SyncedUser::class, TripActivity::class, RoomActivity::class, DeletedEntity::class], version = 5, exportSchema = true,
       )
@TypeConverters(TypeConverter::class)
abstract class PlanningDatabase: RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun userDao(): UserDao

    abstract fun roomActivityDao(): RoomActivityDao
    abstract fun tripActivityDao(): TripActivityDao

    abstract fun deletedEntityDao(): DeletedEntityDao

    companion object {
        private const val DATABASE_NAME = "PLANNING_DATABASE"

        // For Singleton instantiation
        @Volatile
        private var INSTANCE: PlanningDatabase? = null

        /**
         * Get the database instance.
         *
         * @param context The context of the application.
         * @return The database instance.
         */
        fun getDatabase(context: Context): PlanningDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlanningDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}