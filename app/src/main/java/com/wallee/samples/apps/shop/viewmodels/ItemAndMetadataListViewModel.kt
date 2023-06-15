package com.wallee.samples.apps.shop.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wallee.samples.apps.shop.data.ItemAndMetadata
import com.wallee.samples.apps.shop.data.ItemMetadataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemAndMetadataListViewModel
@Inject internal constructor(private val itemMetadataRepository: ItemMetadataRepository) :
    ViewModel() {

    val itemAndMetadata: Flow<List<ItemAndMetadata>> =
        itemMetadataRepository.getDistinctItemMetadata()

    fun deleteItemFromShopCart(itemIds: Long) {
        viewModelScope.launch {
            itemMetadataRepository.deleteItemMetadata(itemIds)
        }
    }
}