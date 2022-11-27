package com.leebeebeom.clothinghelper.util.dragSelect

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo

abstract class BaseSelectedItem {
    abstract val key: String
    abstract val index: Int
}

data class ListSelectedItem(
    override val key: String,
    override val index: Int,
    val top: Int,
    val bottom: Int
) : BaseSelectedItem()

fun LazyListItemInfo.toListSelectedItem() = ListSelectedItem(
    key = key as String,
    index = index,
    top = top,
    bottom = bottom
)

data class GridSelectedItem(
    override val key: String,
    override val index: Int,
    val top: Int,
    val bottom: Int,
    val start: Int,
    val end: Int,
) : BaseSelectedItem()

fun LazyGridItemInfo.toGridSelectedItem() =
    GridSelectedItem(
        key = key as String,
        index = index,
        top = top,
        bottom = bottom,
        start = start,
        end = end
    )

val LazyListItemInfo.top: Int get() = offset + 20
val LazyListItemInfo.bottom: Int get() = top + size

val LazyGridItemInfo.top get() = offset.y
val LazyGridItemInfo.bottom get() = offset.y + size.height
val LazyGridItemInfo.start get() = offset.x
val LazyGridItemInfo.end get() = offset.x + size.width