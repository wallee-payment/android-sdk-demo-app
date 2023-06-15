package com.wallee.samples.apps.shop.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the [ItemMetadata] class.
 */
@Dao
interface ItemMetadataDao {
    @Query("SELECT * FROM item_metadata")
    fun getItemMetadataList(): Flow<List<ItemMetadata>>

    /**
     * This query will tell Room to query both the [Item] and [ItemMetadata] tables and handle
     * the object mapping.
     */
    @Transaction
    @Query("SELECT * FROM items WHERE id IN (SELECT DISTINCT(item_id) FROM item_metadata)")
    fun getDistinctItemMetadata(): Flow<List<ItemAndMetadata>>

    @Insert
    suspend fun insertItemMetadata(itemMetadata: ItemMetadata): Long

    @Delete
    suspend fun deleteItemMetadata(itemMetadata: ItemMetadata)

    @Query("SELECT * FROM item_metadata WHERE id = :id")
    fun getItemMetadata(id: Long): Flow<ItemMetadata>
}
