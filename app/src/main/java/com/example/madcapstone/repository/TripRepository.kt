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

    fun getTrips() = tripDao.getTrips()

    suspend fun insertTrip(trip: Trip) = tripDao.insertTrip(trip)

    @Transaction
    suspend fun deleteTrip(trip: Trip) {
        if (userDao.getUser()!!.lastSync > trip.createdAt) {
            deletedEntityDao.insertDeletedEntity(DeletedEntity(trip.tripId, EntityType.TRIP))
        }
        tripDao.deleteTrip(trip)
    }



    @Transaction
    suspend fun addActivityToTrip(trip: Trip, activity: RoomActivity, date: Date) {
        activityDao.insertRoomActivity(activity)
        tripActivityDao.insertTripActivity(TripActivity(trip.tripId, activity.activityId, date))
        val count = tripDao.getActivitiesForTripCount(trip.tripId)
        if (count == 1) {
            tripDao.updateTrip(trip.copy(imageUrl = activity.imageUrl))
        }
    }

    fun getTripActivities(tripId: String, date: Date) = tripDao.getTripActivities(tripId, date)

    fun getTripsWithoutActivity(activityId: String) = tripDao.getTripsWithoutActivity(activityId)

    fun getTripsCount() = tripDao.getTripsCount()
}