package com.wallee.samples.apps.shop.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wallee.samples.apps.shop.data.Item
import com.wallee.samples.apps.shop.data.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemListViewModel @Inject internal constructor(
    itemRepository: ItemRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val defaultState: MutableStateFlow<Int> = MutableStateFlow(DEFAULT_KEY)
    val items: StateFlow<List<Item>> = itemRepository.getItems()
        .flatMapLatest { items -> flowOf(items) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        viewModelScope.launch {
            defaultState.collect { key ->
                savedStateHandle[SAVED_STATE_KEY] = key
            }
        }
    }

    companion object {
        private const val DEFAULT_KEY = -1
        private const val SAVED_STATE_KEY = "SAVED_STATE_KEY"
    }
}