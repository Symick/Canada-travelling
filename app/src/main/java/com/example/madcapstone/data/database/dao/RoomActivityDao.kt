package com.example.madcapstone.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.madcapstone.data.models.roomModels.RoomActivity

/**
 * Data Access Object for the roomActivityTable.
 * This are locally stored activities from firebase.
 *
 * @author Julian Kruithof
*/
@Dao
interface RoomActivityDao {

    /**
     * Inserts a RoomActivity into the roomActivityTable.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRoomActivity(roomActivity: RoomActivity)

    /**
     * Inserts a list of RoomActivities into the roomActivityTable.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoomActivities(roomActivities: List<RoomActivity>)
}