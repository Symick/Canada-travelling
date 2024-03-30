package com.example.madcapstone.repository

import android.content.Context
import com.example.madcapstone.data.database.PlanningDatabase
import com.example.madcapstone.data.database.dao.TripActivityDao
import com.example.madcapstone.data.models.roomModels.TripActivity

class TripActivityRepository(context: Context) {
    private val tripActivityDao: TripActivityDao

    init {
        val planningDatabase = PlanningDatabase.getDatabase(context)
        tripActivityDao = planningDatabase.tripActivityDao()
    }

    suspend fun deleteTripActivity(tripActivity: TripActivity) = tripActivityDao.deleteTripActivity(tripActivity)

    suspend fun updateTripActivity(tripActivity: TripActivity) = tripActivityDao.updateTripActivity(tripActivity)
}