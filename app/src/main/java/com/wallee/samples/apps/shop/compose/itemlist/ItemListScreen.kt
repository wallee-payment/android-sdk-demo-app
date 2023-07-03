package com.wallee.samples.apps.shop.compose.itemlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.dimensionResource
import com.wallee.samples.apps.shop.R
import com.wallee.samples.apps.shop.data.Item
import com.wallee.samples.apps.shop.viewmodels.ItemListViewModel

@Composable
fun ItemListScreen(
    onItemClick: (Item) -> Unit,
    itemListViewModel: ItemListViewModel,
) {
    val items by itemListViewModel.items.collectAsState()


    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.card_side_margin),
            vertical = dimensionResource(id = R.dimen.header_margin)
        )
    ) {
        items(
            items = items,
            key = { it.itemId }
        ) { item ->
            ListItem(item = item) {
                onItemClick(item)
            }
        }
    }
}