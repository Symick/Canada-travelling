package com.example.madcapstone.repository

import android.content.Context
import android.util.Log
import androidx.room.Transaction
import com.example.madcapstone.data.database.PlanningDatabase
import com.example.madcapstone.data.database.dao.DeletedEntityDao
import com.example.madcapstone.data.database.dao.RoomActivityDao
import com.example.madcapstone.data.database.dao.TripActivityDao
import com.example.madcapstone.data.database.dao.TripDao
import com.example.madcapstone.data.database.dao.UserDao
import com.example.madcapstone.data.models.roomModels.DeletedEntity
import com.example.madcapstone.data.models.roomModels.EntityType
import com.example.madcapstone.data.models.roomModels.RoomActivity
import com.example.madcapstone.data.models.roomModels.Trip
import com.example.madcapstone.data.models.roomModels.TripActivity
import java.util.Date

/**
 * Repository class for the Trip.
 * This class is used to handle the Trip objects in the local database.
 *
 * @property tripDao The TripDao
 * @property activityDao The RoomActivityDao
 * @property tripActivityDao The TripActivityDao
 * @property userDao The UserDao
 * @property deletedEntityDao The DeletedEntityDao
 *
 * @author Julian Kruithof
 */
class TripRepository(context: Context) {
    private val tripDao: TripDao
    private val activityDao: RoomActivityDao
    private val tripActivityDao: TripActivityDao
    private val userDao: UserDao
    private val deletedEntityDao: DeletedEntityDao

    init {
        val planningDatabase = PlanningDatabase.getDatabase(context)
        tripDao = planningDatabase.tripDao()
        activityDao = planningDatabase.roomActivityDao()
        tripActivityDao = planningDatabase.tripActivityDao()
        userDao = planningDatabase.userDao()
        deletedEntityDao = planningDatabase.deletedEntityDao()
    }

    /**
     * Function to get all Trip objects from the local database.
     *
     * @return A list of all Trip objects
     */
    fun getTrips() = tripDao.getTrips()

    /**
     * Function to insert a Trip object into the local database.
     *
     * @param trip The Trip object to insert
     */
    suspend fun insertTrip(trip: Trip) = tripDao.insertTrip(trip)

    /**
     * Function to delete a Trip object in the local database.
     * The function also adds a reference to the deleted entity in the DeletedEntity table
     * if the entity was already synced to the firebase database.
     *
     * @param trip The Trip object to delete
     */
    @Transaction
    suspend fun deleteTrip(trip: Trip) {
        if (userDao.getUser()!!.lastSync > trip.createdAt) {
            deletedEntityDao.insertDeletedEntity(DeletedEntity(trip.tripId, EntityType.TRIP))
        }
        tripDao.deleteTrip(trip)
    }



    /**
     * Function to add a activity object to the trip in the local database.
     *
     * @param trip The Trip object to update
     */
    @Transaction
    suspend fun addActivityToTrip(trip: Trip, activity: RoomActivity, date: Date) {
        activityDao.insertRoomActivity(activity)
        tripActivityDao.insertTripActivity(TripActivity(trip.tripId, activity.activityId, date))
        val count = tripDao.getActivitiesForTripCount(trip.tripId)
        if (count == 1) {
            tripDao.updateTrip(trip.copy(imageUrl = activity.imageUrl, updatedAt = Date()))
        }
    }

    /**
     * Function to retrieve all activities for a trip from the local database.
     *
     * @param tripId The id of the trip
     * @param date The date of the activities
     */
    fun getTripActivities(tripId: String, date: Date) = tripDao.getTripActivities(tripId, date)

    /**
     * Function to get trips without a specific activity.
     *
     * @param activityId The id of the activity
     */
    fun getTripsWithoutActivity(activityId: String) = tripDao.getTripsWithoutActivity(activityId)

    /**
     * Function to get the count of the trips in the local database.
     */
    fun getTripsCount() = tripDao.getTripsCount()
}