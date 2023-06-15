package com.wallee.samples.apps.shop.compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints

fun Modifier.visible(isVisible: () -> Boolean) = this.then(VisibleModifier(isVisible))

private data class VisibleModifier(
    private val isVisible: () -> Boolean
) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            if (isVisible()) {
                placeable.place(0, 0)
            }
        }
    }
}
