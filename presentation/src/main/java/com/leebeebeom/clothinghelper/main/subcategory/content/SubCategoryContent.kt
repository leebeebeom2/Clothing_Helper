package com.leebeebeom.clothinghelper.main.subcategory.content

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.main.subcategory.content.header.SubCategoryHeader
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.SortOrder
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySort
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySortPreferences
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun SubCategoryContent(
    parent: SubCategoryParent,
    isAllExpand: () -> Boolean,
    subCategories: () -> ImmutableList<StableSubCategory>,
    sort: () -> SubCategorySortPreferences,
    state: SubCategoryContentState = rememberSubCategoryContentState(),
    allExpandIconClick: () -> Unit,
    onLongClick: (key: String) -> Unit,
    onSubCategoryClick: (StableSubCategory) -> Unit,
    onSortClick: (SubCategorySort) -> Unit,
    onOrderClick: (SortOrder) -> Unit,
    selectedSubCategoryKey: () -> ImmutableSet<String>,
    isSelectMode: () -> Boolean,
    onSelect: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(onDragStart = { offset ->
                        interceptOutOfBoundsChildEvents = true
                        state.dragSelectStart(offset, onLongClick)
                    }, onDrag = { change, _ ->
                        change.consume()
                        state.onDrag(change.position, onSelect)
                        state.onDragEndMove(change.position, onSelect)
                    }, onDragEnd = {
                        interceptOutOfBoundsChildEvents = false
                        state.onDragEnd()
                    })
                }, contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, top = 16.dp, bottom = 120.dp
            ), verticalArrangement = Arrangement.spacedBy(8.dp), state = state.lazyListState
        ) {
            item {
                SubCategoryHeader(
                    parent = parent,
                    isAllExpand = isAllExpand,
                    sort = sort,
                    allExpandIconClick = allExpandIconClick,
                    onSortClick = onSortClick,
                    onOrderClick = onOrderClick
                )
            }
            items(items = subCategories(), key = { it.key }) {
                var isExpanded by rememberSaveable { mutableStateOf(false) }
                SubCategoryCard(
                    subCategory = { it },
                    isAllExpand = isAllExpand,
                    onClick = { if (isSelectMode()) onSelect(it.key) else onSubCategoryClick(it) },
                    selectedCategoryKeys = selectedSubCategoryKey,
                    updateIsExpanded = { isExpanded = it },
                    toggleIsExpanded = { isExpanded = !isExpanded },
                    isExpanded = { isExpanded },
                    isSelectMode = isSelectMode
                )
            }
        }
    }
}

data class SubCategoryContentState(
    val lazyListState: LazyListState,
    val haptic: HapticFeedback,
    var initialSelectedKey: String? = null,
    var initialSelectedTop: Int? = null,
    var initialSelectedBottom: Int? = null,
    val passedItemKeys: MutableSet<String> = mutableSetOf(),
    val interactionSource: InteractionSource
) {
    enum class DragPosition {
        DragUp, DragDown, DragStop
    }

    fun currentDragPosition(touchOffset: Offset): DragPosition? {
        return initialSelectedBottom?.let { bottom ->
            initialSelectedTop?.let { top ->
                if (touchOffset.y.toInt() < top) DragPosition.DragUp
                else if (touchOffset.y.toInt() > bottom) DragPosition.DragDown
                else DragPosition.DragStop
            }
        }
    }

    fun performHaptic() {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    inline fun dragSelectStart(
        touchOffset: Offset, crossinline onLongClick: (key: String) -> Unit
    ) {
        lazyListState.getSelectedItem({
            isSelectedItem(
                touchOffset = touchOffset,
                visibleItem = it
            )
        }) { selectedItem ->
            (selectedItem.key as? String)?.let {
                initialSelectedKey = it
                initialSelectedTop = selectedItem.realOffset
                initialSelectedBottom = selectedItem.offsetEnd
                passedItemKeys.add(it)
                onLongClick(it)
                performHaptic()
            }
        }
    }

    inline fun onDrag(
        touchOffset: Offset, crossinline onSelect: (String) -> Unit
    ) {
        initialSelectedKey?.let { initialSelectedKey ->
            lazyListState.getSelectedItem(
                { initialSelectedKey != it.key as? String },
                { !passedItemKeys.contains(it.key) },
                { isSelectedItem(touchOffset = touchOffset, visibleItem = it) }) {
                (it.key as? String)?.let { key ->
                    onSelect(key)
                    passedItemKeys.add(key)
                }
            }
        }
    }

    fun isSelectedItem(touchOffset: Offset, visibleItem: LazyListItemInfo): Boolean {
        return touchOffset.y.toInt() in visibleItem.realOffset..visibleItem.offsetEnd
    }

    inline fun onDragEndMove(
        touchOffset: Offset, crossinline onSelect: (key: String) -> Unit
    ) {
        when (currentDragPosition(touchOffset)) {
            DragPosition.DragDown -> dragEndMove(
                onSelect = onSelect,
            ) { touchOffset.y.toInt() < it.realOffset }
            DragPosition.DragUp -> dragEndMove(
                onSelect = onSelect,
            ) { touchOffset.y.toInt() > it.offsetEnd }
            else -> {}
        }
    }

    inline fun dragEndMove(
        crossinline onSelect: (key: String) -> Unit,
        crossinline condition: (selectedItem: LazyListItemInfo) -> Boolean
    ) = lazyListState.getSelectedItem({ it.key == passedItemKeys.lastOrNull() }) { selectedItem ->
        if (condition(selectedItem)) {
            (selectedItem.key as? String)?.let { key ->
                onSelect(key)
                passedItemKeys.remove(key)
            }
        }
    }

    fun onDragEnd() {
        initialSelectedKey = null
        initialSelectedTop = null
        initialSelectedBottom = null
        passedItemKeys.clear()
    }

    inline fun LazyListState.getSelectedItem(
        vararg conditions: (visibleItem: LazyListItemInfo) -> Boolean,
        crossinline task: (LazyListItemInfo) -> Unit
    ) {
        layoutInfo.visibleItemsInfo.firstOrNull { visibleItem ->
            conditions.forEach { condition ->
                if (!condition(visibleItem)) return@firstOrNull false
            }
            true
        }?.let { task(it) }
    }
}

@Composable
fun rememberSubCategoryContentState(
    lazyListState: LazyListState = rememberLazyListState(),
    haptic: HapticFeedback = LocalHapticFeedback.current,
    interactionSource: InteractionSource = remember { MutableInteractionSource() }
): SubCategoryContentState {
    return remember {
        SubCategoryContentState(
            lazyListState = lazyListState, haptic = haptic, interactionSource = interactionSource
        )
    }
}

val LazyListItemInfo.realOffset: Int
    get() = offset + 50

val LazyListItemInfo.offsetEnd: Int
    get() = offset + size + 43