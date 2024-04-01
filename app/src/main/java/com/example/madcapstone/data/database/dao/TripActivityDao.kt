package com.example.madcapstone.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.madcapstone.data.models.roomModels.TripActivity
import java.util.Date

@Dao
interface TripActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTripActivity(tripActivity: TripActivity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTripActivities(tripActivities: List<TripActivity>)

    @Delete
    suspend fun deleteTripActivity(tripActivity: TripActivity)

    @Update
    suspend fun updateTripActivity(tripActivity: TripActivity)

    @Query("SELECT * FROM tripActivityTable WHERE tripId = :tripId AND activityId = :activityId")
    fun getTripActivity(tripId: String, activityId: String): LiveData<TripActivity>




    @Query("SELECT * FROM tripActivityTable WHERE updatedAt > :syncDate")
    suspend fun getUpdatedTripActivities(syncDate: Date): List<TripActivity>
}