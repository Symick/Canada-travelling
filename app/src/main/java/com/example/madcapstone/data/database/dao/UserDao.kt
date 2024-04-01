package com.example.madcapstone.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.madcapstone.data.models.roomModels.SyncedUser


@Dao
interface UserDao {

    @Query("SELECT * FROM userTable limit 1")
    suspend fun getUser(): SyncedUser?

    @Insert
    suspend fun insertUser(user: SyncedUser)

    @Delete
    suspend fun deleteUser(user: SyncedUser)

    @Update
    suspend fun updateUser(user: SyncedUser)


}