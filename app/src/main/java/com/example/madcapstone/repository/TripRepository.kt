package com.example.madcapstone.repository

import android.content.Context
import androidx.room.Transaction
import com.example.madcapstone.data.database.PlanningDatabase
import com.example.madcapstone.data.database.dao.RoomActivityDao
import com.example.madcapstone.data.database.dao.TripActivityDao
import com.example.madcapstone.data.database.dao.TripDao
import com.example.madcapstone.data.models.roomModels.RoomActivity
import com.example.madcapstone.data.models.roomModels.Trip
import com.example.madcapstone.data.models.roomModels.TripActivity
import java.util.Date

class TripRepository(context: Context) {
    private val tripDao: TripDao
    private val activityDao: RoomActivityDao
    private val tripActivityDao: TripActivityDao

    init {
        val planningDatabase = PlanningDatabase.getDatabase(context)
        tripDao = planningDatabase.tripDao()
        activityDao = planningDatabase.roomActivityDao()
        tripActivityDao = planningDatabase.tripActivityDao()
    }

    fun getTrips() = tripDao.getTrips()

    suspend fun insertTrip(trip: Trip) = tripDao.insertTrip(trip)

    suspend fun deleteTrip(trip: Trip) = tripDao.deleteTrip(trip)

    @Transaction
    suspend fun addActivityToTrip(trip: Trip, activity: RoomActivity, date: Date) {
        activityDao.insertRoomActivity(activity)
        tripActivityDao.insertTripActivity(TripActivity(trip.tripId, activity.activityId, date))
    }

    fun getTripActivities(tripId: String, date: Date) = tripDao.getTripActivities(tripId, date)
}