package com.example.madcapstone.data.syncWorkers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.madcapstone.repository.SynchronisationRepository

class RoomSyncWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(
    appContext,
    params
) {
    private val syncRepo = SynchronisationRepository(appContext)
    private var firebaseDate = SynchronisationRepository.RetrievedFromFirebase(
        emptyList(),
        emptyList(),
        emptyList()
    )

    override suspend fun doWork(): Result {
        val userId = inputData.getString("userId")
        if (userId.isNullOrBlank()) {
            Log.d("RoomSyncWorker", "No user id provided")
            return Result.failure()
        }

        try {
            firebaseDate = syncRepo.getDataFromFirebase(userId)
        } catch (e: Exception) {
            Log.d("RoomSyncWorker", "Error getting data from firebase: ${e.message}")
            return Result.failure()
        }

        if (firebaseDate.trips.isEmpty() && firebaseDate.tripActivities.isEmpty() && firebaseDate.activities.isEmpty()) {
            return Result.success()
        }
        return try {
            syncRepo.syncRoomDbFromFirebase(firebaseDate, userId)
            Result.success()
        } catch (e: Exception) {
            Log.d("RoomSyncWorker", "Error syncing data to room: ${e.message}")
            Result.failure()
        }
    }
    companion object {
        const val WORK_NAME = "RoomSyncWorker"
        const val USER_ID   = "userId"
    }
}