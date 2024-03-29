package com.example.madcapstone.repository

import android.content.Context
import com.example.madcapstone.data.database.PlanningDatabase
import com.example.madcapstone.data.database.dao.TripDao
import com.example.madcapstone.data.models.roomModels.Trip
import com.google.firebase.Firebase
import com.google.firebase.storage.storage

class TripRepository(context: Context) {
    private val tripDao: TripDao

    init {
        val planningDatabase = PlanningDatabase.getDatabase(context)
        tripDao = planningDatabase.tripDao()
    }

    fun getTrips() = tripDao.getTrips()

    suspend fun insertTrip(trip: Trip) = tripDao.insertTrip(trip)

    suspend fun deleteTrip(trip: Trip) = tripDao.deleteTrip(trip)
}