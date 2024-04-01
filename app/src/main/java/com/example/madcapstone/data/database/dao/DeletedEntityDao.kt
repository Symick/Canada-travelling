package com.example.madcapstone.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.madcapstone.data.models.roomModels.DeletedEntity

@Dao
interface DeletedEntityDao {

    @Insert
    suspend fun insertDeletedEntity(deletedEntity: DeletedEntity)

    @Query("DELETE FROM deletedEntityTable")
    suspend fun deleteAllDeletedEntities()

    @Query("SELECT * FROM deletedEntityTable")
    suspend fun getDeletedEntities(): List<DeletedEntity>
}