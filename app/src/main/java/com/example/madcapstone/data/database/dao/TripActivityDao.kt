package com.example.madcapstone.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.madcapstone.data.models.roomModels.RoomActivity
import com.example.madcapstone.data.models.roomModels.TripActivity

@Dao
interface TripActivityDao {

    @Insert
    suspend fun insertTripActivity(tripActivity: TripActivity)
}