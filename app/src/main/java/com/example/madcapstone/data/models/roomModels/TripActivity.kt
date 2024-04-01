package com.example.madcapstone.data.models.roomModels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.Date

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
