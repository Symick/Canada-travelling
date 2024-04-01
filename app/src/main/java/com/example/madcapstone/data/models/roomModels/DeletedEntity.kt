package com.example.madcapstone.data.models.roomModels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deletedEntityTable")
data class DeletedEntity(
    val tripId: String,
    val type: EntityType,
    val activityId: String? = null,
    @PrimaryKey(autoGenerate = true)
    val entityId: Long = 0,
)

enum class EntityType {
    TRIP,
    ACTIVITY
}