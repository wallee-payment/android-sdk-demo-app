package com.wallee.samples.apps.shop.compose

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

val Shapes.card: CornerBasedShape
    get() = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 12.dp,
        bottomStart = 12.dp,
        bottomEnd = 0.dp
    )