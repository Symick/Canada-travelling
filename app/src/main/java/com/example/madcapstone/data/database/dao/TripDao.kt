package com.example.madcapstone.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.madcapstone.data.models.roomModels.RoomActivity
import com.example.madcapstone.data.models.roomModels.Trip
import java.util.Date

/**
 * Data Access Object for the tripTable.
 * This class is used to interact with the local database.
 *
 * @see Trip
 *
 * @author Julian Kruithof
 */
@Dao
interface TripDao {

    /**
     * Retrieves all Trips from the tripTable.
     */
    @Query("SELECT * FROM tripTable")
    fun getTrips(): LiveData<List<Trip>>

    /**
     * insert a Trip from the tripTable.
     */
    @Insert
    suspend fun insertTrip(trip: Trip)

    /**
     * insert a list of Trips from the tripTable.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrips(trips: List<Trip>)


    /**
     * Deletes a Trip from the tripTable.
     */
    @Delete
    suspend fun deleteTrip(trip: Trip)

    /**
     * Retrieves all activities for a certain trip on a certain date.
     */
    @Query("SELECT activityTable.* FROM tripActivityTable INNER JOIN activityTable ON tripActivityTable.activityId = activityTable.activityId WHERE tripActivityTable.tripId = :tripId AND tripActivityTable.date = :date")
    fun getTripActivities(tripId: String, date: Date): LiveData<List<RoomActivity>>

    /**
     * Retrieves all Trips from the tripTable that are not in the tripActivityTable.
     */
    @Query("SELECT * FROM tripTable WHERE tripId NOT in (SELECT tripId FROM tripActivityTable WHERE activityId == :activityId)")
    fun getTripsWithoutActivity(activityId: String): LiveData<List<Trip>>

    /**
     * Get the number of trips in the tripTable.
     */
    @Query("SELECT COUNT(*) FROM tripTable")
    fun getTripsCount(): LiveData<Int>

    /**
     * Get the number of activities for a certain trip.
     */
    @Query("SELECT COUNT(*) FROM tripActivityTable WHERE tripId = :tripId")
    suspend fun getActivitiesForTripCount(tripId: String): Int

    /**
     * Updates a Trip in the tripTable.
     */
    @Update
    suspend fun updateTrip(trip: Trip)

    /**
     * Retrieves all Trips that needs to be synchronised to remote database.
     */
    @Query("SELECT * FROM tripTable WHERE updatedAt > :syncDate")
    fun getUpdatedTrips(syncDate: Date): List<Trip>
}