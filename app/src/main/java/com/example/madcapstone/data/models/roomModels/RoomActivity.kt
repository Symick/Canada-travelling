package com.example.madcapstone.data.models.roomModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.madcapstone.data.models.firebaseModels.OpeningHours
import com.google.firebase.firestore.DocumentId

@Entity(tableName = "activityTable")
data class RoomActivity(
    @PrimaryKey(autoGenerate = false)
    val activityId: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val websiteUrl: String,
    val location: String,
    val address: String,
    val minPrice: Double = 0.0,
    val maxPrice: Double = 0.0,
    val isFree: Boolean,
    val openingHours: Map<String, OpeningHours>,
)
