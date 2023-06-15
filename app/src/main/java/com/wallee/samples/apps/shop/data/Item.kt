package com.wallee.samples.apps.shop.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey @ColumnInfo(name = "id") val itemId: String,
    val name: String,
    val description: String,
    val price: Int,
    val imageUrl: String = ""
) {
    override fun toString() = name
}
