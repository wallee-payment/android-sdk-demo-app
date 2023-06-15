package com.wallee.samples.apps.shop.data

import androidx.room.Embedded
import androidx.room.Relation

/**
 * This class captures the relationship between a [Item] and a user's [ItemMetadata], which is
 * used by Room to fetch the related entities.
 */
data class ItemAndMetadata(
    @Embedded
    val item: Item,

    @Relation(parentColumn = "id", entityColumn = "item_id")
    val itemMetadata: List<ItemMetadata> = emptyList()
)
