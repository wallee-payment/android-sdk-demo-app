package com.wallee.samples.apps.shop.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wallee.samples.apps.shop.R

/**
 * Class that captures dimens used in Compose code. The dimens that need to be consistent with the
 * View system use [dimensionResource] and are marked as composable.
 *
 * Disclaimer:
 * This approach doesn't consider multiple configurations. For that, an Ambient should be created.
 */
object Dimens {

    val PaddingSmall: Dp
        @Composable get() = dimensionResource(R.dimen.margin_small)

    val PaddingNormal: Dp
        @Composable get() = dimensionResource(R.dimen.margin_normal)

    val PaddingLarge: Dp = 24.dp

    val ItemDetailAppBarHeight: Dp
        @Composable get() = dimensionResource(R.dimen.item_detail_app_bar_height)

    val ToolbarIconPadding = 12.dp

    val ToolbarIconSize = 32.dp
}
