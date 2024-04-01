package com.example.madcapstone.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.madcapstone.data.models.roomModels.RoomActivity
import com.example.madcapstone.data.models.roomModels.Trip
import java.util.Date

@Dao
interface TripDao {

    @Query("SELECT * FROM tripTable")
    fun getTrips(): LiveData<List<Trip>>

    @Insert
    suspend fun insertTrip(trip: Trip)

    @Delete
    suspend fun deleteTrip(trip: Trip)

    @Query("SELECT activityTable.* FROM tripActivityTable INNER JOIN activityTable ON tripActivityTable.activityId = activityTable.activityId WHERE tripActivityTable.tripId = :tripId AND tripActivityTable.date = :date")
    fun getTripActivities(tripId: String, date: Date): LiveData<List<RoomActivity>>

    @Query("SELECT * FROM tripTable WHERE tripId NOT in (SELECT tripId FROM tripActivityTable WHERE activityId == :activityId)")
    fun getTripsWithoutActivity(activityId: String): LiveData<List<Trip>>

    @Query("SELECT COUNT(*) FROM tripTable")
    fun getTripsCount(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM tripActivityTable WHERE tripId = :tripId")
    suspend fun getActivitiesForTripCount(tripId: String): Int

    @Update
    suspend fun updateTrip(trip: Trip)

    @Query("SELECT * FROM tripTable WHERE updatedAt > :syncDate")
    fun getUpdatedTrips(syncDate: Date): List<Trip>
}