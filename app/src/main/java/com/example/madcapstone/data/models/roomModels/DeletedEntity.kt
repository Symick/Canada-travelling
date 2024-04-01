package com.example.madcapstone.data.models.roomModels

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class for the DeletedEntity object.
 * This class is used to store the deleted entities from the local database.
 * This class is used for synchronisation purposes.
 *
 * @param tripId The id of the trip the entity belongs to
 * @param type The type of the entity
 * @param activityId The id of the activity
 * @param entityId The id of the entity
 */
@Entity(tableName = "deletedEntityTable")
data class DeletedEntity(
    val tripId: String,
    val type: EntityType,
    val activityId: String? = null,
    @PrimaryKey(autoGenerate = true)
    val entityId: Long = 0,
)

/**
 * Enum class for the EntityType.
 * This determines what entity was deleted in the local database.
 */
enum class EntityType {
    TRIP,
    ACTIVITY
}