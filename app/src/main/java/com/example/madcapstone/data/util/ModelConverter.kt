package com.example.madcapstone.data.util

import com.example.madcapstone.data.models.firebaseModels.FirestoreActivity
import com.example.madcapstone.data.models.firebaseModels.FirestoreTrip
import com.example.madcapstone.data.models.roomModels.RoomActivity
import com.example.madcapstone.data.models.roomModels.Trip

/**
 * Class for converting Firestore models to Room models and vice versa.
 *
 * @author Julian Kruithof
 */
class ModelConverter {
    companion object {

        /**
         * Converts a FirestoreActivity to a RoomActivity
         *
         * @param firestoreActivity The FirestoreActivity to convert
         *
         *  @return The RoomActivity
         */
        fun convertToRoomActivity(firestoreActivity: FirestoreActivity): RoomActivity {
            return RoomActivity(
                activityId = firestoreActivity.id,
                name = firestoreActivity.name,
                description = firestoreActivity.description,
                imageUrl = firestoreActivity.imageUrl,
                websiteUrl = firestoreActivity.websiteUrl,
                location = firestoreActivity.Location,
                address = firestoreActivity.address,
                minPrice = firestoreActivity.minPrice,
                maxPrice = firestoreActivity.maxPrice,
                isFree = firestoreActivity.isFree,
                openingHours = firestoreActivity.openingHours
            )
        }

        /**
         * Converts a RoomActivity to a FirestoreActivity
         *
         * @param roomActivity The RoomActivity to convert
         *
         * @return The FirestoreActivity
         */
        fun convertToFirestoreActivity(roomActivity: RoomActivity): FirestoreActivity {
            return FirestoreActivity(
                id = roomActivity.activityId,
                name = roomActivity.name,
                description = roomActivity.description,
                imageUrl = roomActivity.imageUrl,
                websiteUrl = roomActivity.websiteUrl,
                Location = roomActivity.location,
                address = roomActivity.address,
                minPrice = roomActivity.minPrice,
                maxPrice = roomActivity.maxPrice,
                isFree = roomActivity.isFree,
                openingHours = roomActivity.openingHours
            )
        }

        /**
         * Converts a RoomTrip to a FirestoreTrip
         *
         * @param roomTrip The RoomTrip to convert
         * @param userId The id of the user that owns the trip
         *
         * @return The FirestoreTrip
         */
        fun convertToFirestoreTrip(roomTrip: Trip, userId: String): FirestoreTrip {
            return FirestoreTrip(
                tripId = roomTrip.tripId,
                userId = userId,
                title = roomTrip.title,
                imageUrl = roomTrip.imageUrl,
                startDate = roomTrip.startDate,
                endDate = roomTrip.endDate,
                createdAt = roomTrip.createdAt
            )
        }

        /**
         * Converts a FirestoreTrip to a RoomTrip
         *
         * @param firestoreTrip The FirestoreTrip to convert
         *
         * @return The RoomTrip
         */
        fun convertToRoomTrip(firestoreTrip: FirestoreTrip): Trip {
            return Trip(
                tripId = firestoreTrip.tripId,
                title = firestoreTrip.title,
                imageUrl = firestoreTrip.imageUrl,
                startDate = firestoreTrip.startDate,
                endDate = firestoreTrip.endDate,
                createdAt = firestoreTrip.createdAt
            )
        }
    }
}