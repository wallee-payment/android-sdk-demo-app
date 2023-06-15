package com.wallee.samples.apps.shop.compose.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.wallee.samples.apps.shop.R
import com.wallee.samples.apps.shop.ShopActivity
import com.wallee.samples.apps.shop.compose.cart.ShopCartScreen
import com.wallee.samples.apps.shop.compose.config.ConfigScreen
import com.wallee.samples.apps.shop.compose.itemlist.ItemListScreen
import com.wallee.samples.apps.shop.data.Item
import com.wallee.samples.apps.shop.data.Settings
import com.wallee.samples.apps.shop.databinding.HomeScreenBinding
import kotlinx.coroutines.launch

enum class WalleeShopPage(
    @StringRes val titleResId: Int,
    @DrawableRes val drawableResId: Int
) {
    ITEMS_LIST(R.string.item_list_title, R.drawable.ic_item_list_active),
    MY_SHOPPING_CART(R.string.my_shopping_cart_title, R.drawable.ic_shopping_bag),
    CONFIG(R.string.app_config_title, R.drawable.ic_config)
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onItemClick: (Item) -> Unit = {},
    onPageChange: (WalleeShopPage) -> Unit = {},
    onAttached: (Toolbar) -> Unit = {},
    launchSdk: (String) -> Unit,
    cacheSettings: (Settings) -> Unit,
) {
    val activity = (LocalContext.current as ShopActivity)

    AndroidViewBinding(factory = HomeScreenBinding::inflate, modifier = modifier) {
        onAttached(toolbar)
        activity.setSupportActionBar(toolbar)
        composeView.setContent {
            HomePagerScreen(
                onItemClick = onItemClick,
                onPageChange = onPageChange,
                launchSdk = launchSdk,
                cacheSettings = cacheSettings,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePagerScreen(
    onItemClick: (Item) -> Unit,
    onPageChange: (WalleeShopPage) -> Unit,
    modifier: Modifier = Modifier,
    pages: Array<WalleeShopPage> = WalleeShopPage.values(),
    launchSdk: (String) -> Unit,
    cacheSettings: (Settings) -> Unit,
) {
    val pagerState = rememberPagerState()

    LaunchedEffect(pagerState.currentPage) {
        onPageChange(pages[pagerState.currentPage])
    }

    Column(modifier.nestedScroll(rememberNestedScrollInteropConnection())) {
        val coroutineScope = rememberCoroutineScope()

        TabRow(selectedTabIndex = pagerState.currentPage) {
            pages.forEachIndexed { index, page ->
                val title = stringResource(id = page.titleResId)
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(text = title) },
                    icon = {
                        Icon(
                            painter = painterResource(id = page.drawableResId),
                            contentDescription = title
                        )
                    },
                    unselectedContentColor = MaterialTheme.colors.primaryVariant,
                    selectedContentColor = MaterialTheme.colors.onPrimary,
                )
            }
        }

        val activity = (LocalContext.current as ShopActivity)
        // Pages
        HorizontalPager(
            pageCount = pages.size,
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { index ->
            when (pages[index]) {
                WalleeShopPage.MY_SHOPPING_CART -> {
                    ShopCartScreen(
                        configViewModel = activity.configViewModel,
                        resultViewModel = activity.resultViewModel,
                        modifier = Modifier.fillMaxSize(),
                        onAddItemClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(WalleeShopPage.ITEMS_LIST.ordinal)
                            }
                        },
                        onEmptyConfigClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(WalleeShopPage.CONFIG.ordinal)
                            }
                        },
                        onItemClick = {
                            onItemClick(it.item)
                        },
                        launchSdk = {
                            launchSdk(it)
                        }
                    )
                }
                WalleeShopPage.ITEMS_LIST -> {
                    ItemListScreen(
                        onItemClick = onItemClick,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                WalleeShopPage.CONFIG -> {
                    ConfigScreen(
                        configViewModel = activity.configViewModel,
                        cacheSettings = cacheSettings,
                    )
                }
            }
        }
    }
}