package com.example.madcapstone.data.syncWorkers

import android.content.Context
import android.util.Log
import androidx.room.Transaction
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.madcapstone.data.database.PlanningDatabase
import com.example.madcapstone.data.database.dao.TripActivityDao
import com.example.madcapstone.data.database.dao.TripDao
import com.example.madcapstone.data.database.dao.UserDao
import com.example.madcapstone.data.models.roomModels.Trip
import com.example.madcapstone.data.models.roomModels.TripActivity
import com.example.madcapstone.repository.SynchronisationRepository

/**
 * Worker class that syncs the local room database with the firebase database.
 * When fired it retrieves all database changes made locally and pushes it to the firebase database.
 *
 * @param appContext The application context
 * @param params The worker parameters
 *
 * @return Result.success() if the sync was successful, Result.failure() otherwise
 *
 * @author Julian Kruithof
 */
class TripsSyncWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext,
    params
) {
    private val syncRepo = SynchronisationRepository(appContext)

    /**
     * The work that should be done by the worker.
     * i.e. sync local changes to firebase
     */
    override suspend fun doWork(): Result {
        val dataToSync = syncRepo.getDataToSync()
        if (dataToSync.trips.isEmpty() && dataToSync.tripActivities.isEmpty() && dataToSync.deletedEntities.isEmpty()) {
            return Result.success()
        }
        return try {
            syncRepo.syncTripsToFirebase(dataToSync)
            Result.success()
        } catch (e: Exception) {
            Log.d("TripsSyncWorker", "Error syncing trips to firebase: ${e.message}")
            Result.failure()
        }
    }
}

