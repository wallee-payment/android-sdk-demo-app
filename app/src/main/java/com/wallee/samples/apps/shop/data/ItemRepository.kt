package com.wallee.samples.apps.shop.data

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository module for handling data operations.
 *
 * Collecting from the Flows in [ItemDao] is main-safe.  Room supports Coroutines and moves the
 * query execution off of the main thread.
 */
@Singleton
class ItemRepository @Inject constructor(private val itemDao: ItemDao) {

    fun getItems() = itemDao.getItems()

    fun getItem(itemId: String) = itemDao.getItem(itemId)

    companion object {

        @Volatile
        private var instance: ItemRepository? = null

        fun getInstance(itemDao: ItemDao) =
            instance ?: synchronized(this) {
                instance ?: ItemRepository(itemDao).also { instance = it }
            }
    }
}
