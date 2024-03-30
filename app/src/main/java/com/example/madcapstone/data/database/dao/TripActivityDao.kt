package com.example.madcapstone.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.madcapstone.data.models.roomModels.RoomActivity
import com.example.madcapstone.data.models.roomModels.TripActivity

@Dao
interface TripActivityDao {

    @Insert
    suspend fun insertTripActivity(tripActivity: TripActivity)

    @Delete
    suspend fun deleteTripActivity(tripActivity: TripActivity)

    @Update
    suspend fun updateTripActivity(tripActivity: TripActivity)
}