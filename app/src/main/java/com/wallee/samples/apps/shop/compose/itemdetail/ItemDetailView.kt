package com.wallee.samples.apps.shop.compose.itemdetail

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.themeadapter.material.MdcTheme
import com.wallee.samples.apps.shop.R
import com.wallee.samples.apps.shop.compose.Dimens
import com.wallee.samples.apps.shop.compose.utils.ItemImage
import com.wallee.samples.apps.shop.compose.utils.TextSnackbarContainer
import com.wallee.samples.apps.shop.compose.visible
import com.wallee.samples.apps.shop.data.Item
import com.wallee.samples.apps.shop.viewmodels.ItemDetailViewModel

data class ItemDetailsCallbacks(
    val onFabClick: () -> Unit,
    val onBackClick: () -> Unit,
)

@Composable
fun ItemDetailsScreen(
    itemDetailViewModel: ItemDetailViewModel,
    onBackClick: () -> Unit,
) {
    val item by itemDetailViewModel.item.collectAsState()
    val showSnackbarState by itemDetailViewModel.showSnackbar.collectAsState()

    item?.let {
        ItemDetails(
            item!!,
            ItemDetailsCallbacks(
                onBackClick = onBackClick,
                onFabClick = {
                    itemDetailViewModel.addItemToShopCart()
                }
            )
        )
    }

    if (showSnackbarState) {
        TextSnackbarContainer(
            snackbarText = stringResource(R.string.added_item_to_shop_cart),
            showSnackbar = showSnackbarState,
            onDismissSnackbar = { itemDetailViewModel.dismissSnackbar() }
        ) {
        }
    }
}

@Composable
fun ItemDetails(
    item: Item,
    callbacks: ItemDetailsCallbacks,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var itemScroller by remember {
        mutableStateOf(ItemDetailsScroller(scrollState, Float.MIN_VALUE))
    }
    val transitionState =
        remember(itemScroller) { itemScroller.toolbarTransitionState }
    val toolbarState = itemScroller.getToolbarState(LocalDensity.current)

    val transition = updateTransition(transitionState, label = "")
    val toolbarAlpha = transition.animateFloat(
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) }, label = ""
    ) { toolbarTransitionState ->
        if (toolbarTransitionState == ToolbarState.HIDDEN) 0f else 1f
    }
    val contentAlpha = transition.animateFloat(
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) }, label = ""
    ) { toolbarTransitionState ->
        if (toolbarTransitionState == ToolbarState.HIDDEN) 1f else 0f
    }

    val toolbarHeightPx = with(LocalDensity.current) {
        Dimens.ItemDetailAppBarHeight.roundToPx().toFloat()
    }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value =
                    newOffset.coerceIn(-toolbarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    Box(
        modifier
            .fillMaxSize()
            .systemBarsPadding()
            .nestedScroll(nestedScrollConnection)
    ) {
        ItemDetailsContent(
            scrollState = scrollState,
            toolbarState = toolbarState,
            onNamePosition = { newNamePosition ->
                if (itemScroller.namePosition == Float.MIN_VALUE) {
                    itemScroller =
                        itemScroller.copy(namePosition = newNamePosition)
                }
            },
            item = item,
            imageHeight = with(LocalDensity.current) {
                val candidateHeight =
                    Dimens.ItemDetailAppBarHeight + toolbarOffsetHeightPx.value.toDp()
                maxOf(candidateHeight, 1.dp)
            },
            onFabClick = callbacks.onFabClick,
            contentAlpha = { contentAlpha.value }
        )
        ItemToolbar(
            toolbarState, item.name, callbacks,
            toolbarAlpha = { toolbarAlpha.value },
            contentAlpha = { contentAlpha.value }
        )
    }
}

@Composable
private fun ItemDetailsContent(
    scrollState: ScrollState,
    toolbarState: ToolbarState,
    item: Item,
    imageHeight: Dp,
    onNamePosition: (Float) -> Unit,
    onFabClick: () -> Unit,
    contentAlpha: () -> Float,
) {
    Column(Modifier.verticalScroll(scrollState)) {
        ConstraintLayout {
            val (image, info) = createRefs()

            ItemTagImage(
                imageUrl = item.imageUrl,
                imageHeight = imageHeight,
                modifier = Modifier
                    .constrainAs(image) { top.linkTo(parent.top) }
                    .alpha(contentAlpha())
            )
            ItemInformation(
                name = item.name,
                price = item.price,
                description = item.description,
                onNamePosition = { onNamePosition(it) },
                toolbarState = toolbarState,
                modifier = Modifier.constrainAs(info) {
                    top.linkTo(image.bottom)
                },
                onFabClick = onFabClick
            )
        }
    }
}

@Composable
private fun ItemTagImage(
    imageUrl: String,
    imageHeight: Dp,
    modifier: Modifier = Modifier,
    placeholderColor: Color = MaterialTheme.colors.onSurface.copy(0.2f)
) {
    val isLoading by remember { mutableStateOf(true) }
    Box(
        modifier
            .fillMaxWidth()
            .height(imageHeight)
    ) {
        if (isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(placeholderColor)
            )
        }
        ItemImage(
            model = imageUrl,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
private fun ItemToolbar(
    toolbarState: ToolbarState,
    itemName: String,
    callbacks: ItemDetailsCallbacks,
    toolbarAlpha: () -> Float,
    contentAlpha: () -> Float
) {
    if (toolbarState.isShown) {
        ItemDetailsToolbar(
            itemName = itemName,
            onBackClick = callbacks.onBackClick,
            modifier = Modifier.alpha(toolbarAlpha())
        )
    } else {
        ItemHeaderActions(
            onBackClick = callbacks.onBackClick,
            modifier = Modifier.alpha(contentAlpha())
        )
    }
}

@Composable
private fun ItemDetailsToolbar(
    itemName: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface {
        TopAppBar(
            modifier = modifier.statusBarsPadding(),
            backgroundColor = MaterialTheme.colors.surface
        ) {
            IconButton(
                onBackClick,
                Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = ""
                )
            }
            Text(
                text = itemName,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}

@Composable
private fun ItemHeaderActions(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(top = Dimens.ToolbarIconPadding),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val iconModifier = Modifier
            .sizeIn(
                maxWidth = Dimens.ToolbarIconSize,
                maxHeight = Dimens.ToolbarIconSize
            )
            .background(
                color = MaterialTheme.colors.surface,
                shape = CircleShape
            )

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(start = Dimens.ToolbarIconPadding)
                .then(iconModifier)
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = ""
            )
        }
    }
}

@Composable
private fun ItemInformation(
    name: String,
    price: Int,
    description: String,
    onNamePosition: (Float) -> Unit,
    toolbarState: ToolbarState,
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit,
) {
    Column(modifier = modifier.padding(Dimens.PaddingLarge)) {
        Text(
            text = name,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(
                    start = Dimens.PaddingSmall,
                    end = Dimens.PaddingSmall,
                    bottom = Dimens.PaddingNormal
                )
                .align(Alignment.CenterHorizontally)
                .onGloballyPositioned { onNamePosition(it.positionInWindow().y) }
                .visible { toolbarState == ToolbarState.HIDDEN }
        )

        Text(
            text = description,
            modifier = Modifier
                .padding(
                    start = Dimens.PaddingSmall,
                    end = Dimens.PaddingSmall,
                    bottom = Dimens.PaddingNormal
                )
                .align(Alignment.Start)
                .onGloballyPositioned { onNamePosition(it.positionInWindow().y) }
        )
        BuyButton(price, onFabClick)
    }
}

@Composable
private fun BuyButton(
    price: Int,
    onFabClick: () -> Unit,
) {
    Button(
        onClick = onFabClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = dimensionResource(id = R.dimen.button_corner_radius),
            bottomStart = dimensionResource(id = R.dimen.button_corner_radius),
            bottomEnd = 0.dp,
        )
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "Price tag",
            modifier = Modifier.size(ButtonDefaults.IconSize),
            tint = MaterialTheme.colors.onPrimary
        )
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = "$price CHF",
            color = MaterialTheme.colors.onPrimary
        )
    }
}

@Preview
@Composable
private fun ItemDetailContentPreview() {
    MdcTheme {
        Surface {
            ItemDetails(
                Item("itemId", "Tomato", "HTML<br>description", 6, "url"),
                ItemDetailsCallbacks({ }, { })
            )
        }
    }
}