package com.example.madcapstone.data.models.roomModels

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "userTable")
data class SyncedUser(
    @PrimaryKey(autoGenerate = false)
    var id : String, // Firebase user id
)
