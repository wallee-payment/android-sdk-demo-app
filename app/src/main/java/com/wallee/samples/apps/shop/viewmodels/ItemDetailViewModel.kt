package com.wallee.samples.apps.shop.viewmodels

import androidx.lifecycle.*
import com.wallee.samples.apps.shop.data.ItemMetadataRepository
import com.wallee.samples.apps.shop.data.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    itemRepository: ItemRepository,
    private val itemMetadataRepository: ItemMetadataRepository,
) : ViewModel() {

    val itemId: String = savedStateHandle.get<String>(ITEM_ID_SAVED_STATE_KEY)!!

    val item = itemRepository.getItem(itemId).asLiveData()

    private val _showSnackbar = MutableLiveData(false)
    val showSnackbar: LiveData<Boolean>
        get() = _showSnackbar

    fun addItemToShopCart() {
        viewModelScope.launch {
            itemMetadataRepository.createItemMetadata(itemId)
            _showSnackbar.value = true
        }
    }

    fun dismissSnackbar() {
        _showSnackbar.value = false
    }

    companion object {
        private const val ITEM_ID_SAVED_STATE_KEY = "itemId"
    }
}