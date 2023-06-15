package com.wallee.samples.apps.shop.data

import androidx.room.*

@Entity(
    tableName = "item_metadata",
    foreignKeys = [
        ForeignKey(entity = Item::class, parentColumns = ["id"], childColumns = ["item_id"])
    ],
    indices = [Index("item_id")]
)
data class ItemMetadata(
    @ColumnInfo(name = "item_id") val itemId: String,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var itemMetadataId: Long = 0
}
