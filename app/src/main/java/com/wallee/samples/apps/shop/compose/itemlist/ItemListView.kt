package com.wallee.samples.apps.shop.compose.itemlist

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import com.wallee.samples.apps.shop.R
import com.wallee.samples.apps.shop.compose.card
import com.wallee.samples.apps.shop.compose.utils.ItemImage
import com.wallee.samples.apps.shop.data.Item

@Composable
fun ListItem(item: Item, onClick: () -> Unit) {
    ImageListItem(name = item.name, price = item.price, imageUrl = item.imageUrl, onClick = onClick)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImageListItem(name: String, price: Int, imageUrl: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        elevation = dimensionResource(id = R.dimen.card_elevation),
        shape = MaterialTheme.shapes.card,
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.card_side_margin))
            .padding(bottom = dimensionResource(id = R.dimen.card_bottom_margin))
    ) {
        Column(Modifier.fillMaxWidth()) {
            ItemImage(
                model = imageUrl,
                Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.item_image_height)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = name,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(id = R.dimen.margin_normal))
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            Text(
                text = price.toString() + "CHF",
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(id = R.dimen.margin_normal))
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}