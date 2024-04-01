package com.example.madcapstone.data.models.roomModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

/**
 * Data class for the Trip object.
 * This class is used to store the trips in the local database.
 *
 * @param title The title of the trip
 * @param startDate The start date of the trip
 * @param endDate The end date of the trip
 * @param imageUrl The image url of the trip
 * @param createdAt The creation date of the trip
 * @param updatedAt The last update date of the trip
 * @param tripId The id of the trip
 */
@Entity(tableName = "tripTable")
data class Trip(
    val title: String,
    val startDate: Date,
    val endDate: Date,
    val imageUrl: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    @PrimaryKey(autoGenerate = false)
    val tripId: String = UUID.randomUUID().toString(),
)
