package com.example.madcapstone.data.models.firebaseModels

import java.util.Date

data class FirestoreTrip(
    val tripId: String = "",
    val userId: String = "",
    val title: String = "",
    val imageUrl: String? = null,
    val startDate: Date = Date(),
    val endDate: Date = Date(),
    val createdAt: Date = Date(),
)