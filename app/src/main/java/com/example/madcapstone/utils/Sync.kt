package com.example.madcapstone.utils

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.madcapstone.data.syncWorkers.RoomSyncWorker
import com.example.madcapstone.data.syncWorkers.TripsSyncWorker

/**
 * Class used to create sync workers.
 *
 * @author Julian Kruithof
 */
class Sync {
    companion object {

        /**
         * Sync trips to Firebase.
         *
         * @param context The context
         */
        fun syncTripsToFirebase(context: Context) {
            val syncWork = "Syncing to Firebase"
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = OneTimeWorkRequest.Builder(TripsSyncWorker::class.java)
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(syncWork,ExistingWorkPolicy.REPLACE, request)
        }

        /**
         * Sync room database from Firebase.
         *
         * @param context The context
         * @param userId The user id
         */
        fun syncRoomDbFromFirebase(context: Context, userId: String) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = OneTimeWorkRequest.Builder(RoomSyncWorker::class.java)
                .setInputData(Data.Builder().putString(RoomSyncWorker.USER_ID, userId).build())
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(RoomSyncWorker.WORK_NAME,ExistingWorkPolicy.REPLACE, request)
        }
    }
}