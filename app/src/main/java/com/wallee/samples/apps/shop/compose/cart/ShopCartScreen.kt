package com.wallee.samples.apps.shop.compose.cart

import androidx.activity.compose.ReportDrawn
import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.wallee.samples.apps.shop.R
import com.wallee.samples.apps.shop.compose.card
import com.wallee.samples.apps.shop.compose.config.*
import com.wallee.samples.apps.shop.compose.utils.ItemImage
import com.wallee.samples.apps.shop.compose.utils.TextSnackbarContainer
import com.wallee.samples.apps.shop.data.ItemAndMetadata
import com.wallee.samples.apps.shop.data.Settings
import com.wallee.samples.apps.shop.data.portal.LineItems
import com.wallee.samples.apps.shop.data.portal.Transaction
import com.wallee.samples.apps.shop.viewmodels.*
import com.wallee.walleepaymentsdk.enums.PaymentResultEnum
import java.util.*

@Composable
fun ShopCartScreen(
    modifier: Modifier = Modifier,
    itemAndMetadataListViewModel: ItemAndMetadataListViewModel,
    configViewModel: ConfigViewModel,
    resultViewModel: ResultViewModel,
    portalViewModel: PortalViewModel,
    onAddItemClick: () -> Unit,
    onEmptyConfigClick: () -> Unit,
    onItemClick: (ItemAndMetadata) -> Unit,
    launchSdk: (String) -> Unit
) {
    val itemList by itemAndMetadataListViewModel.itemAndMetadata.collectAsState(initial = emptyList())
    val showPaymentResult by resultViewModel.showPaymentResult.collectAsState()

    if (itemList.isEmpty()) {
        EmptyShopCart(onAddItemClick, modifier)
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(
                    bottom = dimensionResource(id = R.dimen.gallery_header_margin),
                )
        ) {
            ShopList(itemList = itemList, onItemClick = onItemClick)

            Column(
                modifier = Modifier
                    .padding(
                        start = dimensionResource(id = R.dimen.card_side_margin),
                        end = dimensionResource(id = R.dimen.card_side_margin),
                    )
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
            ) {
                Checkout(
                    showPaymentResult,
                    resultViewModel,
                    itemList,
                    itemAndMetadataListViewModel,
                    configViewModel,
                    portalViewModel,
                    launchSdk,
                    onEmptyConfigClick
                )
            }
        }
    }
}

@Composable
private fun Checkout(
    showPaymentResult: Boolean,
    resultViewModel: ResultViewModel,
    itemList: List<ItemAndMetadata>,
    viewModel: ItemAndMetadataListViewModel,
    configViewModel: ConfigViewModel,
    portalViewModel: PortalViewModel,
    launchSdk: (String) -> Unit,
    onEmptyConfigClick: () -> Unit
) {
    if (showPaymentResult) {
        Snackbar(
            action = {
                Button(
                    onClick = {
                        if (resultViewModel.getPaymentResult() == PaymentResultEnum.COMPLETED.toString()) {
                            itemList.forEach { itm ->
                                itm.itemMetadata.forEach {
                                    viewModel.deleteItemFromShopCart(it.itemMetadataId)
                                }
                            }
                        }
                        resultViewModel.dismissPaymentResultSnackbar()
                    },
                    shape = MaterialTheme.shapes.card,
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                ) {
                    Text(text = stringResource(R.string.confirm_ok))
                }
            }, modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = stringResource(
                    id = R.string.payment_result, resultViewModel.getPaymentResult()
                )

            )
        }
    }

    val (snackbarVisibleState, setSnackBarState) = remember { mutableStateOf(false) }

    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            if (configViewModel.isConfigSettingsEmpty()) {
                setSnackBarState(true)
            } else {
                val transaction = createTransaction(itemList.sumOf { item -> item.item.price })
                val settings = Settings(
                    configViewModel.getUserId(),
                    configViewModel.getSpaceId(),
                    configViewModel.getAuthenticationKey()
                )
                portalViewModel.createToken(transaction, settings, launchSdk)
            }
        }) {
        Text(
            color = MaterialTheme.colors.onPrimary,
            textAlign = TextAlign.Center,
            text = stringResource(R.string.pay_message, itemList.sumOf { item -> item.item.price })
        )
    }

    val showException by portalViewModel.showResult.collectAsState()
    if (showException) {
        TextSnackbarContainer(
            snackbarText = "Failed because of token creation/transaction! Check your credentials!",
            showSnackbar = showException,
            onDismissSnackbar = { portalViewModel.dismissResult() }
        ) {}
    }

    if (snackbarVisibleState) {
        Snackbar(
            action = {
                Button(
                    onClick = {
                        onEmptyConfigClick()
                        setSnackBarState(false)
                    },
                    shape = MaterialTheme.shapes.card,
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                ) {
                    Text(
                        text = stringResource(id = R.string.add_config)
                    )
                }
            }, modifier = Modifier.padding(8.dp)
        ) {
            Text(text = stringResource(id = R.string.config_miss))
        }
    }
}

@Composable
private fun ShopList(
    itemList: List<ItemAndMetadata>,
    onItemClick: (ItemAndMetadata) -> Unit,
) {
    val gridState = rememberLazyGridState()
    ReportDrawnWhen { gridState.layoutInfo.totalItemsCount > 0 }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState
    ) {
        items(items = itemList, key = { it.item.itemId }) {
            ShopListItem(itemAndMetadata = it, onItemClick = onItemClick)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ShopListItem(
    itemAndMetadata: ItemAndMetadata, onItemClick: (ItemAndMetadata) -> Unit
) {
    val vm = ItemsWithMetadataViewModel(itemAndMetadata)
    val cardSideMargin = dimensionResource(id = R.dimen.card_side_margin)
    val marginNormal = dimensionResource(id = R.dimen.margin_normal)

    Card(
        onClick = { onItemClick(itemAndMetadata) },
        modifier = Modifier.padding(
            start = cardSideMargin,
            end = cardSideMargin,
            bottom = dimensionResource(id = R.dimen.card_bottom_margin)
        ),
        elevation = dimensionResource(id = R.dimen.card_elevation),
        shape = MaterialTheme.shapes.card,
    ) {
        Column(Modifier.fillMaxWidth()) {
            ItemImage(
                model = vm.imageUrl,
                Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.item_image_height)),
                contentScale = ContentScale.Fit,
            )

            // Item name
            Text(
                text = vm.itemName,
                Modifier
                    .padding(vertical = marginNormal)
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.subtitle1,
            )
            // Price
            Text(
                text = stringResource(id = R.string.price_header),
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = marginNormal),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = vm.itemPrice.toString() + " CHF",
                Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Composable
private fun EmptyShopCart(onAddItemClick: () -> Unit, modifier: Modifier = Modifier) {
    ReportDrawn()
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.shop_cart_empty),
            style = MaterialTheme.typography.h5
        )
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            shape = MaterialTheme.shapes.card,
            onClick = onAddItemClick
        ) {
            Text(
                color = MaterialTheme.colors.onPrimary,
                text = stringResource(id = R.string.add_item)
            )
        }
    }
}


fun createTransaction(sumOf: Int): String {
    val myLineItem = LineItems(
        amountIncludingTax = sumOf,
        name = "items-demo-shop",
        quantity = 1,
        shippingRequired = false,
        sku = "items-demo-shop",
        type = "PRODUCT",
        uniqueId = "items-demo-shop"
    )

    val body = Transaction(
        currency = "CHF",
        language = Locale.getDefault().toLanguageTag(),
        lineItems = arrayListOf(myLineItem)
    )

    return Gson().toJson(body)
}
