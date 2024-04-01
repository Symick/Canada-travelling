package com.example.madcapstone.data.models.roomModels

import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.Date


/**
 * Data class for the TripActivity object.
 * This class is used to store the activities that are part of a trip in the local database.
 *
 * @param tripId The id of the trip the activity belongs to
 * @param activityId The id of the activity
 * @param date The date of the activity
 * @param createdAt The creation date of the activity
 * @param updatedAt The last update date of the activity
 */
@Entity(
    tableName = "tripActivityTable",
    primaryKeys = ["tripId", "activityId"],
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["tripId"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RoomActivity::class,
            parentColumns = ["activityId"],
            childColumns = ["activityId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TripActivity(
    val tripId: String = "",
    val activityId: String = "",
    val date: Date = Date(),
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)
