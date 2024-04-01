package com.example.madcapstone.data.models.firebaseModels

import java.util.Date

/**
 * Data class for the FirestoreTrip object.
 * This is the firestore representation of a trip @See [com.example.madcapstone.data.models.roomModels.Trip]
 * This is a separate class since local database trips don't have a userId field.
 */
data class FirestoreTrip(
    val tripId: String = "",
    val userId: String = "",
    val title: String = "",
    val imageUrl: String? = null,
    val startDate: Date = Date(),
    val endDate: Date = Date(),
    val createdAt: Date = Date(),
)