package com.example.madcapstone.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
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
}