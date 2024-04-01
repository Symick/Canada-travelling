package com.example.madcapstone.data.models.roomModels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "tripTable")
data class Trip(
    val title: String,
    val startDate: Date,
    val endDate: Date,
    val imageUrl: String? = null,
    val createdAt : Date = Date(),
    val updatedAt: Date = Date(),
    @PrimaryKey(autoGenerate = false)
    val tripId: String = UUID.randomUUID().toString(),
)
