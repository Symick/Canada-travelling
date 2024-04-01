package com.example.madcapstone.data.models.roomModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.madcapstone.data.models.firebaseModels.OpeningHours
import com.google.firebase.firestore.DocumentId

/**
 * Data class for the RoomActivity object.
 * This class is used to store the activity information of a location.
 *
 * @param activityId The id of the activity
 * @param name The name of the activity
 * @param description The description of the activity
 * @param imageUrl The url of the image of the activity
 * @param websiteUrl The url of the website of the activity
 * @param location The location of the activity
 * @param address The address of the activity
 * @param minPrice The minimum price of the activity
 * @param maxPrice The maximum price of the activity
 * @param isFree A boolean that indicates if the activity is free
 * @param openingHours A map of the opening hours of the activity
 *
 * @property activityId The id of the activity
 */
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
