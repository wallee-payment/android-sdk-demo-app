package com.wallee.samples.apps.shop.compose

import androidx.appcompat.widget.Toolbar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wallee.samples.apps.shop.ShopActivity
import com.wallee.samples.apps.shop.compose.home.HomeScreen
import com.wallee.samples.apps.shop.compose.home.WalleeShopPage
import com.wallee.samples.apps.shop.compose.itemdetail.ItemDetailsScreen

@Composable
fun WalleeShopApp(
    onPageChange: (WalleeShopPage) -> Unit = {},
    onAttached: (Toolbar) -> Unit = {},
) {
    val navController = rememberNavController()
    WalleeShopNavHost(
        navController = navController,
        onPageChange = onPageChange,
        onAttached = onAttached
    )
}

@Composable
fun WalleeShopNavHost(
    navController: NavHostController,
    onPageChange: (WalleeShopPage) -> Unit = {},
    onAttached: (Toolbar) -> Unit = {},
) {
    val activity = (LocalContext.current as ShopActivity)
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onItemClick = {
                    navController.navigate("itemDetail/${it.itemId}")
                },
                launchSdk = {
                    activity.launchSdkFromActivity(it)
                },
                cacheSettings = {
                    activity.savePreferences(it)
                },
                onPageChange = onPageChange,
                onAttached = onAttached
            )
        }
        composable(
            "itemDetail/{itemId}",
            arguments = listOf(navArgument("itemId") {
                type = NavType.StringType
            })
        ) {
            ItemDetailsScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}