package com.example.madcapstone.repository

import android.content.Context
import android.util.Log
import androidx.room.Transaction
import com.example.madcapstone.data.database.PlanningDatabase
import com.example.madcapstone.data.database.dao.DeletedEntityDao
import com.example.madcapstone.data.database.dao.RoomActivityDao
import com.example.madcapstone.data.database.dao.TripActivityDao
import com.example.madcapstone.data.database.dao.TripDao
import com.example.madcapstone.data.database.dao.UserDao
import com.example.madcapstone.data.models.firebaseModels.FirestoreActivity
import com.example.madcapstone.data.models.firebaseModels.FirestoreTrip
import com.example.madcapstone.data.models.roomModels.DeletedEntity
import com.example.madcapstone.data.models.roomModels.EntityType
import com.example.madcapstone.data.models.roomModels.RoomActivity
import com.example.madcapstone.data.models.roomModels.SyncedUser
import com.example.madcapstone.data.models.roomModels.Trip
import com.example.madcapstone.data.models.roomModels.TripActivity
import com.example.madcapstone.data.util.ModelConverter
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.util.Date
import kotlin.math.log

class SynchronisationRepository(context: Context) {
    private val db: PlanningDatabase
    private val tripDao: TripDao
    private val userDao: UserDao
    private val tripActivityDao: TripActivityDao
    private val deletedEntityDao: DeletedEntityDao
    private val roomActivityDao: RoomActivityDao
    private val store = Firebase.firestore

    init {
        db = PlanningDatabase.getDatabase(context)
        tripDao = db.tripDao()
        userDao = db.userDao()
        tripActivityDao = db.tripActivityDao()
        deletedEntityDao = db.deletedEntityDao()
        roomActivityDao = db.roomActivityDao()
    }

    data class ToSynced(
        val trips: List<Trip>,
        val tripActivities: List<TripActivity>,
        val deletedEntities: List<DeletedEntity>,
        val syncedUserId: String? = null,
    )

    data class RetrievedFromFirebase(
        val trips: List<Trip>,
        val tripActivities: List<TripActivity>,
        val activities: List<RoomActivity>
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
                        val subCollectionRef =
                            docRef.collection("activities").document(deletedEntity.activityId!!)
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

    suspend fun getDataFromFirebase(userId: String): RetrievedFromFirebase {
        val trips = mutableListOf<Trip>()
        val tripActivities = mutableListOf<TripActivity>()
        val activities = mutableListOf<RoomActivity>()
        Log.d("SynchronisationRepository", "Getting data from firebase {userId: $userId}")

        val retrievedData = store.collection("trips").whereEqualTo("userId", userId).get().await()
        retrievedData.forEach{ tripDoc ->
            val trip = tripDoc.toObject(FirestoreTrip::class.java)
            trips.add(ModelConverter.convertToRoomTrip(trip))
            tripDoc.reference.collection("activities").get().await().forEach { activityDoc ->
                val activity = activityDoc.toObject(TripActivity::class.java)
                tripActivities.add(activity)
                store.collection("activities").document(activity.activityId).get().await().toObject(FirestoreActivity::class.java)?.let {
                    activities.add(ModelConverter.convertToRoomActivity(it))
                }
            }
        }
        return RetrievedFromFirebase(trips, tripActivities, activities)
    }

    @Transaction
    suspend fun syncRoomDbFromFirebase(retrievedData: RetrievedFromFirebase, userId: String) {
        db.clearAllTables()
        tripDao.insertTrips(retrievedData.trips)
        roomActivityDao.insertRoomActivities(retrievedData.activities)
        tripActivityDao.insertTripActivities(retrievedData.tripActivities)
        userDao.insertUser(SyncedUser(userId, Date()))
    }
}


