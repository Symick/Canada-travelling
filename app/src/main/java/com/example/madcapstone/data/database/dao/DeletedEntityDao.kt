package com.example.madcapstone.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.madcapstone.data.models.roomModels.DeletedEntity

/**
 * Data Access Object for the deletedEntityTable.
 *
 * This class is used for synchronisation purposes. It keeps track of entities that have been deleted locally and need to be deleted from the remote database.
 *
 * @see DeletedEntity
 *
 * @author Julian Kruithof
 */
@Dao
interface DeletedEntityDao {

    /**
     * Inserts a DeletedEntity into the deletedEntityTable.
     */
    @Insert
    suspend fun insertDeletedEntity(deletedEntity: DeletedEntity)

    /**
     * Deletes all DeletedEntities from the deletedEntityTable.
     */
    @Query("DELETE FROM deletedEntityTable")
    suspend fun deleteAllDeletedEntities()

    /**
     * Retrieves all DeletedEntities from the deletedEntityTable.
     */
    @Query("SELECT * FROM deletedEntityTable")
    suspend fun getDeletedEntities(): List<DeletedEntity>
}