package com.wanandroid.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun FlowLayout(
    modifier: Modifier = Modifier,
    rowHorizontalGravity: Alignment.Horizontal = Alignment.Start,
    childVerticalGravity: Alignment.Vertical = Alignment.Top,
    children: @Composable () -> Unit
) {
    class RowInfo(val width: Int, val height: Int, val nextChildIndex: Int)

    Layout(
        content = children,
        modifier = modifier
    ) { measurables, constraints ->
        var contentWidth = 0
        var contentHeight = 0
        var rowWidth = 0
        var rowHeight = 0
        val rows = mutableListOf<RowInfo>()
        val maxWidth = constraints.maxWidth
        val childConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val placeables = measurables.mapIndexed { index, measurable ->
            measurable.measure(childConstraints).also { placeable ->
                val newRowWidth = rowWidth + placeable.width
                if (newRowWidth > maxWidth) {
                    rows.add(RowInfo(width = rowWidth, height = rowHeight, nextChildIndex = index))
                    contentWidth = maxOf(contentWidth, rowWidth)
                    contentHeight += rowHeight
                    rowWidth = placeable.width
                    rowHeight = placeable.height
                } else {
                    rowWidth = newRowWidth
                    rowHeight = maxOf(rowHeight, placeable.height)
                }
            }
        }
        rows.add(RowInfo(width = rowWidth, height = rowHeight, nextChildIndex = measurables.size))
        contentWidth = maxOf(contentWidth, rowWidth)
        contentHeight += rowHeight

        layout(
            width = maxOf(contentWidth, constraints.minWidth),
            height = maxOf(contentHeight, constraints.minHeight)
        ) {
            var childIndex = 0
            var y = 0
            rows.forEach { rowInfo ->
                var x =
                    rowHorizontalGravity.align(maxWidth - rowInfo.width, 10, LayoutDirection.Ltr)
                val rowHeight = rowInfo.height
                val nextChildIndex = rowInfo.nextChildIndex
                while (childIndex < nextChildIndex) {
                    val placeable = placeables[childIndex]
                    placeable.placeRelative(
                        x = x,
                        y = y + childVerticalGravity.align(rowHeight - placeable.height, 10)
                    )
                    x += placeable.width
                    childIndex++
                }
                y += rowHeight
            }
        }
    }
}