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

/**
 * Repository class for the synchronisation.
 * This class is used to sync the local room database with the firebase database.
 *
 * @param context The application context
 * @property db The PlanningDatabase instance
 * @property tripDao The TripDao
 * @property userDao The UserDao
 * @property tripActivityDao The TripActivityDao
 * @property deletedEntityDao The DeletedEntityDao
 * @property roomActivityDao The RoomActivityDao
 * @property store The Firestore database instance
 *
 * @constructor Creates a new SynchronisationRepository
 */
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

    /**
     * Data class for the data to sync.
     * This class is used to store the data that needs to be synced to the firebase database.
     *
     * @param trips The list of trips
     * @param tripActivities The list of trip activities
     * @param deletedEntities The list of deleted entities
     * @param syncedUserId The id of the user that is synced
     */
    data class ToSynced(
        val trips: List<Trip>,
        val tripActivities: List<TripActivity>,
        val deletedEntities: List<DeletedEntity>,
        val syncedUserId: String? = null,
    )

    /**
     * Data class for the data retrieved from firebase.
     * This class is used to store the data that is retrieved from the firebase database.
     *
     * @param trips The list of trips
     * @param tripActivities The list of trip activities
     * @param activities The list of activities
     */
    data class RetrievedFromFirebase(
        val trips: List<Trip>,
        val tripActivities: List<TripActivity>,
        val activities: List<RoomActivity>
    )

    /**
     * Function to get the data that needs to be synced to the firebase database.
     *
     * @return The data that needs to be synced
     */
    @Transaction
    suspend fun getDataToSync(): ToSynced {
        val user = userDao.getUser() ?: return ToSynced(emptyList(), emptyList(), emptyList())
        val trips = tripDao.getUpdatedTrips(user.lastSync)
        val tripActivities = tripActivityDao.getUpdatedTripActivities(user.lastSync)
        val deletedEntities = deletedEntityDao.getDeletedEntities()
        return ToSynced(trips, tripActivities, deletedEntities, user.id)
    }

    /**
     * Function to sync the trips to the firebase database.
     *
     * @param syncDate The data that needs to be synced
     */
    suspend fun syncTripsToFirebase(syncDate: ToSynced) {
        store.runBatch { batch ->
            syncDate.trips.forEach { trip ->
                val tripObj = ModelConverter.convertToFirestoreTrip(trip, syncDate.syncedUserId!!)
                val tripRef = store.collection(TRIPS_REF).document(trip.tripId)
                batch.set(tripRef, tripObj)
            }

            syncDate.tripActivities.forEach { tripActivity ->
                val subCollectionRef = store.collection(TRIPS_REF).document(tripActivity.tripId)
                    .collection(ACTIVITIES_REF).document(tripActivity.activityId)
                batch.set(subCollectionRef, tripActivity)
            }

            for (deletedEntity in syncDate.deletedEntities) {
                val docRef = store.collection(TRIPS_REF).document(deletedEntity.tripId)
                when (deletedEntity.type) {
                    EntityType.TRIP -> {
                        batch.delete(docRef)
                        deleteSubCollection(docRef)
                    }

                    EntityType.ACTIVITY -> {
                        val subCollectionRef =
                            docRef.collection(ACTIVITIES_REF).document(deletedEntity.activityId!!)
                        batch.delete(subCollectionRef)
                    }
                }
            }
        }
        deletedEntityDao.deleteAllDeletedEntities()
        userDao.updateUser(SyncedUser(syncDate.syncedUserId!!, Date()))
    }

    /**
     * helper function to delete a sub collection of a document
     * For now this works fine. However when application grows (and i am willing to spent money) Cloud functions are a better solution
     *
     * @param tripRef The reference to the trip
     *
     * @return The data that needs to be synced
     */
    private fun deleteSubCollection(tripRef: DocumentReference) {
        val newBatch = store.batch()
        tripRef.collection(ACTIVITIES_REF).get().addOnSuccessListener { querySnapshot ->
            querySnapshot.documents.forEach { doc ->
                newBatch.delete(doc.reference)
            }
            newBatch.commit()
        }
    }

    /**
     * Function to get the data from the firebase database.
     *
     * @param userId The id of the user
     *
     * @return The data retrieved from the firebase database
     */
    suspend fun getDataFromFirebase(userId: String): RetrievedFromFirebase {
        val trips = mutableListOf<Trip>()
        val tripActivities = mutableListOf<TripActivity>()
        val activities = mutableListOf<RoomActivity>()

        val retrievedData = store.collection(TRIPS_REF).whereEqualTo(USER_ID_REF, userId).get().await()
        retrievedData.forEach{ tripDoc ->
            val trip = tripDoc.toObject(FirestoreTrip::class.java)
            trips.add(ModelConverter.convertToRoomTrip(trip))
            tripDoc.reference.collection(ACTIVITIES_REF).get().await().forEach { activityDoc ->
                val activity = activityDoc.toObject(TripActivity::class.java)
                tripActivities.add(activity)
                store.collection(ACTIVITIES_REF).document(activity.activityId).get().await().toObject(FirestoreActivity::class.java)?.let {
                    activities.add(ModelConverter.convertToRoomActivity(it))
                }
            }
        }
        return RetrievedFromFirebase(trips, tripActivities, activities)
    }

    /**
     * Function to sync the local room database with the firebase database.
     *
     * @param retrievedData The data retrieved from the firebase database
     * @param userId The id of the user
     */
    @Transaction
    suspend fun syncRoomDbFromFirebase(retrievedData: RetrievedFromFirebase, userId: String) {
        db.clearAllTables()
        tripDao.insertTrips(retrievedData.trips)
        roomActivityDao.insertRoomActivities(retrievedData.activities)
        tripActivityDao.insertTripActivities(retrievedData.tripActivities)
        userDao.insertUser(SyncedUser(userId, Date()))
    }

    companion object {
        private const val TRIPS_REF = "trips"
        private const val ACTIVITIES_REF = "activities"
        private const val USER_ID_REF = "userId"
    }
}


