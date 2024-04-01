package com.example.madcapstone.data.models.roomModels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "userTable")
data class SyncedUser(
    @PrimaryKey(autoGenerate = false)
    var id: String, // Firebase user id
    @ColumnInfo(name = "lastSync")
    val lastSync: Date = Date(),
)
