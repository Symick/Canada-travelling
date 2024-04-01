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

/**
 * Data Access Object for the tripActivityTable.
 * This class is used to interact with the local database.
 *
 * @see TripActivity
 *
 * @author Julian Kruithof
 */
@Dao
interface TripActivityDao {

    /**
     * Retrieves all TripActivities from the tripActivityTable.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTripActivity(tripActivity: TripActivity)

    /**
     * Retrieves all TripActivities from the tripActivityTable.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTripActivities(tripActivities: List<TripActivity>)

    /**
     * Deletes all TripActivities from the tripActivityTable.
     */
    @Delete
    suspend fun deleteTripActivity(tripActivity: TripActivity)

    /**
     * Updates a TripActivity in the tripActivityTable.
     */
    @Update
    suspend fun updateTripActivity(tripActivity: TripActivity)

    /**
     * Retrieves all TripActivities from the tripActivityTable.
     */
    @Query("SELECT * FROM tripActivityTable WHERE tripId = :tripId AND activityId = :activityId")
    fun getTripActivity(tripId: String, activityId: String): LiveData<TripActivity>

    /**
     * Retrieves all TripActivities that needs to be synchronised to remote database.
     */
    @Query("SELECT * FROM tripActivityTable WHERE updatedAt > :syncDate")
    suspend fun getUpdatedTripActivities(syncDate: Date): List<TripActivity>
}