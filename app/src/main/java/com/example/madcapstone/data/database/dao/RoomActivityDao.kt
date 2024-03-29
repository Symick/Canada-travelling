package com.example.madcapstone.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.madcapstone.data.models.roomModels.RoomActivity

@Dao
interface RoomActivityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRoomActivity(roomActivity: RoomActivity)
}