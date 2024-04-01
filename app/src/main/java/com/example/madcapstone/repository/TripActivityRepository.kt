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

/**
 * Repository class for the TripActivity.
 * This class is used to handle the TripActivity objects in the local database.
 *
 * @property tripActivityDao The TripActivityDao
 * @property userDao The UserDao
 * @property deletedEntityDao The DeletedEntityDao
 *
 * @author Julian Kruithof
 */
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

    /**
     * Function to insert a TripActivity object into the local database.
     *
     * @param tripActivity The TripActivity object to insert
     */
    @Transaction
    suspend fun deleteTripActivity(tripActivity: TripActivity) {
        if (userDao.getUser()!!.lastSync > tripActivity.createdAt) {
            deletedEntityDao.insertDeletedEntity(
                DeletedEntity(
                    tripActivity.tripId,
                    EntityType.ACTIVITY,
                    tripActivity.activityId
                )
            )
        }
        tripActivityDao.deleteTripActivity(tripActivity)
    }

    /**
     * Function to get a TripActivity object from the local database.
     *
     * @param tripId The id of the trip the activity belongs to
     * @param activityId The id of the activity
     */
    fun getTripActivity(tripId: String, activityId: String) =
        tripActivityDao.getTripActivity(tripId, activityId)

    /**
     * Function to update a TripActivity object into the local database.
     *
     * @param tripActivity The TripActivity object to update
     */
    suspend fun updateTripActivity(tripActivity: TripActivity) =
        tripActivityDao.updateTripActivity(tripActivity)
}