package com.example.madcapstone.repository

import android.content.Context
import android.util.Log
import androidx.room.Transaction
import com.example.madcapstone.data.database.PlanningDatabase
import com.example.madcapstone.data.database.dao.DeletedEntityDao
import com.example.madcapstone.data.database.dao.TripActivityDao
import com.example.madcapstone.data.database.dao.TripDao
import com.example.madcapstone.data.database.dao.UserDao
import com.example.madcapstone.data.models.roomModels.DeletedEntity
import com.example.madcapstone.data.models.roomModels.EntityType
import com.example.madcapstone.data.models.roomModels.SyncedUser
import com.example.madcapstone.data.models.roomModels.Trip
import com.example.madcapstone.data.models.roomModels.TripActivity
import com.example.madcapstone.data.util.ModelConverter
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.util.Date

class SynchronisationRepository(context: Context) {
    private val tripDao: TripDao
    private val userDao: UserDao
    private val tripActivityDao: TripActivityDao
    private val deletedEntityDao: DeletedEntityDao
    private val store = Firebase.firestore

    init {
        val db = PlanningDatabase.getDatabase(context)
        tripDao = db.tripDao()
        userDao = db.userDao()
        tripActivityDao = db.tripActivityDao()
        deletedEntityDao = db.deletedEntityDao()
    }

    data class ToSynced(
        val trips: List<Trip>,
        val tripActivities: List<TripActivity>,
        val deletedEntities: List<DeletedEntity>,
        val syncedUserId: String? = null,
    )

    @Transaction
    suspend fun getDataToSync(): ToSynced {
        val user = userDao.getUser() ?: return ToSynced(emptyList(), emptyList(), emptyList())
        val trips = tripDao.getUpdatedTrips(user.lastSync)
        val tripActivities = tripActivityDao.getUpdatedTripActivities(user.lastSync)
        val deletedEntities = deletedEntityDao.getDeletedEntities()
        return ToSynced(trips, tripActivities, deletedEntities, user.id)
    }

    suspend fun syncTripsToFirebase(syncDate: ToSynced) {
        store.runBatch { batch ->
            syncDate.trips.forEach { trip ->
                val tripObj = ModelConverter.convertToFirestoreTrip(trip, syncDate.syncedUserId!!)
                val tripRef = store.collection("trips").document(trip.tripId)
                batch.set(tripRef, tripObj)
            }

            syncDate.tripActivities.forEach { tripActivity ->
                val subCollectionRef = store.collection("trips").document(tripActivity.tripId)
                    .collection("activities").document(tripActivity.activityId)
                batch.set(subCollectionRef, tripActivity)
            }

            for (deletedEntity in syncDate.deletedEntities) {
                val docRef = store.collection("trips").document(deletedEntity.tripId)
                when (deletedEntity.type) {
                    EntityType.TRIP -> {
                        batch.delete(docRef)
                        deleteSubCollection(docRef)
                    }
                    EntityType.ACTIVITY -> {
                        val subCollectionRef = docRef.collection("activities").document(deletedEntity.activityId!!)
                        batch.delete(subCollectionRef)
                    }
                }
            }
        }
        deletedEntityDao.deleteAllDeletedEntities()
        userDao.updateUser(SyncedUser(syncDate.syncedUserId!!, Date()))
    }

    private fun deleteSubCollection(tripRef: DocumentReference) {
        val newBatch = store.batch()
        tripRef.collection("activities").get().addOnSuccessListener { querySnapshot ->
            querySnapshot.documents.forEach { doc ->
                newBatch.delete(doc.reference)
            }
            newBatch.commit()
        }
    }
}


