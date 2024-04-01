package com.example.madcapstone.data.models.roomModels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


/**
 * Data class for the SyncedUser object.
 * This class is used to store the user that is currently Synced. i.e. for this user the activities and trips are stored locally
 * This class is used for synchronisation purposes.
 *
 * @param id The id of the user
 * @param lastSync The last time the user was synced
 */
@Entity(tableName = "userTable")
data class SyncedUser(
    @PrimaryKey(autoGenerate = false)
    var id: String, // Firebase user id
    @ColumnInfo(name = "lastSync")
    val lastSync: Date = Date(),
)
