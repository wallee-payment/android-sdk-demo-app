package com.wallee.samples.apps.shop.viewmodels

import com.wallee.samples.apps.shop.data.ItemAndMetadata

class ItemsWithMetadataViewModel(itms: ItemAndMetadata) {

    private val item = checkNotNull(itms.item)

    val imageUrl
        get() = item.imageUrl
    val itemName
        get() = item.name
    val itemId
        get() = item.itemId
    val itemPrice
        get() = item.price

}