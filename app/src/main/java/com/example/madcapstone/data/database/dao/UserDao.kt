package com.example.madcapstone.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.madcapstone.data.models.roomModels.SyncedUser


/**
 * Data Access Object for the userTable.
 * This class is used to interact with the local database.
 *
 * @see SyncedUser
 *
 * @author Julian Kruithof
 */
@Dao
interface UserDao {

    /**
     * Retrieves the user from the userTable.
     */
    @Query("SELECT * FROM userTable limit 1")
    suspend fun getUser(): SyncedUser?

    /**
     * Inserts a User into the userTable.
     */
    @Insert
    suspend fun insertUser(user: SyncedUser)

    /**
     * Deletes a User from the userTable.
     */
    @Delete
    suspend fun deleteUser(user: SyncedUser)

    /**
     * Updates a User in the userTable.
     */
    @Update
    suspend fun updateUser(user: SyncedUser)


}