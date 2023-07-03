package com.wallee.samples.apps.shop.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wallee.samples.apps.shop.data.Item
import com.wallee.samples.apps.shop.data.ItemMetadataRepository
import com.wallee.samples.apps.shop.data.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val itemMetadataRepository: ItemMetadataRepository,
) : ViewModel() {

    private val _itemId = MutableStateFlow("")
    val itemId: StateFlow<String>
        get() = _itemId

    private val _item = MutableStateFlow<Item?>(null)
    val item: StateFlow<Item?>
        get() = _item

    private val _showSnackbar = MutableStateFlow(false)
    val showSnackbar: StateFlow<Boolean>
        get() = _showSnackbar

    fun setItemId(itemId: String) {
        _itemId.value = itemId
        fetchItem()
    }

    private fun fetchItem() {
        viewModelScope.launch {
            val item = itemRepository.getItem(itemId.value).firstOrNull()
            _item.value = item
        }
    }

    fun addItemToShopCart() {
        viewModelScope.launch {
            itemMetadataRepository.createItemMetadata(itemId.value)
            _showSnackbar.value = true
        }
    }

    fun dismissSnackbar() {
        _showSnackbar.value = false
    }
}