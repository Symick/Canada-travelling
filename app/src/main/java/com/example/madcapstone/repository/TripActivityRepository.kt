package com.example.madcapstone.repository

import android.content.Context
import androidx.room.Transaction
import com.example.madcapstone.data.database.PlanningDatabase
import com.example.madcapstone.data.database.dao.DeletedEntityDao
import com.example.madcapstone.data.database.dao.TripActivityDao
import com.example.madcapstone.data.database.dao.UserDao
import com.example.madcapstone.data.models.roomModels.DeletedEntity
import com.example.madcapstone.data.models.roomModels.EntityType
import com.example.madcapstone.data.models.roomModels.TripActivity

class TripActivityRepository(context: Context) {
    private val tripActivityDao: TripActivityDao
    private val userDao: UserDao
    private val deletedEntityDao: DeletedEntityDao

    init {
        val planningDatabase = PlanningDatabase.getDatabase(context)
        tripActivityDao = planningDatabase.tripActivityDao()
        userDao = planningDatabase.userDao()
        deletedEntityDao = planningDatabase.deletedEntityDao()
    }

    @Transaction
    suspend fun deleteTripActivity(tripActivity: TripActivity) {
        if (userDao.getUser()!!.lastSync > tripActivity.createdAt){
            deletedEntityDao.insertDeletedEntity(DeletedEntity(tripActivity.tripId, EntityType.ACTIVITY, tripActivity.activityId))
        }
        tripActivityDao.deleteTripActivity(tripActivity)
    }

    fun getTripActivity(tripId: String, activityId: String) = tripActivityDao.getTripActivity(tripId, activityId)

    suspend fun updateTripActivity(tripActivity: TripActivity) = tripActivityDao.updateTripActivity(tripActivity)
}