package com.wallee.samples.apps.shop.data

import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemMetadataRepository @Inject constructor(
    private val itemMetadataDao: ItemMetadataDao
) {

    suspend fun createItemMetadata(itemId: String) {
        val itemMetadata = ItemMetadata(itemId)
        itemMetadataDao.insertItemMetadata(itemMetadata)
    }

    suspend fun deleteItemMetadata(id: Long) {
        val itemMetadata = itemMetadataDao.getItemMetadata(id).first()
        itemMetadataDao.deleteItemMetadata(itemMetadata)
    }

    fun getDistinctItemMetadata() = itemMetadataDao.getDistinctItemMetadata()

    companion object {

        @Volatile
        private var instance: ItemMetadataRepository? = null

        fun getInstance(itemMetadataDao: ItemMetadataDao) =
            instance ?: synchronized(this) {
                instance ?: ItemMetadataRepository(itemMetadataDao).also { instance = it }
            }
    }
}
