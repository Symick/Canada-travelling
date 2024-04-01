package com.example.madcapstone.utils

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.madcapstone.data.syncWorkers.TripsSyncWorker

class Sync {
    companion object {
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
    }
}