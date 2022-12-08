package com.leebeebeom.clothinghelper.util.dragSelect

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo

abstract class LazyBaseItem {
    abstract val key: String
    abstract val index: Int
}

data class LazyListItem(
    override val key: String,
    override val index: Int,
    val top: Int,
    val bottom: Int
) : LazyBaseItem()

fun LazyListItemInfo.toLazyListItem() = LazyListItem(
    key = key as String,
    index = index,
    top = top,
    bottom = bottom
)

data class LazyGridItem(
    override val key: String,
    override val index: Int,
    val top: Int,
    val bottom: Int,
    val start: Int,
    val end: Int,
) : LazyBaseItem()

fun LazyGridItemInfo.toLazyGridItem() =
    LazyGridItem(
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