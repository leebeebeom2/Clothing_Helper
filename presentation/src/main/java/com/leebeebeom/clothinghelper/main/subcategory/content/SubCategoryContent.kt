package com.leebeebeom.clothinghelper.main.subcategory.content

import androidx.compose.animation.core.Transition
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.main.subcategory.content.header.SubCategoryHeader
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.SortOrder
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySort
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySortPreferences

@Composable
fun SubCategoryContent(
    parent: () -> SubCategoryParent,
    selectModeTransition: Transition<Boolean>,
    isAllExpand: () -> Boolean,
    subCategories: () -> List<SubCategory>,
    isChecked: (SubCategory) -> Boolean,
    sort: () -> SubCategorySortPreferences,
    state: SubCategoryContentState = rememberSubCategoryContentState(
        selectModeTransition,
        subCategories
    ),
    allExpandIconClick: () -> Unit,
    onLongClick: (SubCategory) -> Unit,
    onSubCategoryClick: (SubCategory) -> Unit,
    onSortClick: (SubCategorySort) -> Unit,
    onOrderClick: (SortOrder) -> Unit,
    onAddCategoryPositiveButtonClick: (String, SubCategoryParent) -> Unit
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
                        state.onDrag(change.position, onLongClick)
                        state.onDragEndMove(change.position, onLongClick)
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
            items(items = subCategories(), key = { subCategory -> subCategory.key }) {
                SubCategoryCard(
                    subCategory = { it },
                    isAllExpand = isAllExpand,
                    isChecked = { isChecked(it) },
                    onClick = { onSubCategoryClick(it) },
                    selectModeTransition = selectModeTransition
                )
            }
        }

        AddSubcategoryDialogFab(
            onPositiveButtonClick = { onAddCategoryPositiveButtonClick(it, parent()) },
            subCategories = subCategories
        )
    }
}

data class SubCategoryContentState(
    val lazyListState: LazyListState,
    val haptic: HapticFeedback,
    private val subCategories: List<SubCategory>,
    private var initialSelectedKeyState: String? = null,
    private var initialSelectedTopState: Int? = null,
    private var initialSelectedBottomState: Int? = null,
    private val passedItemKeys: MutableSet<String> = mutableSetOf(),
    val interactionSource: InteractionSource,
    val selectModeTransition: Transition<Boolean>
) {
    companion object {
        private const val isDragStop = 0
        private const val isDragUp = 1
        private const val isDragDown = 2
    }

    private fun currentDragPosition(touchOffset: Offset): Int? {
        return initialSelectedBottomState?.let { bottom ->
            initialSelectedTopState?.let { top ->
                if (touchOffset.y.toInt() < top) isDragUp
                else if (touchOffset.y.toInt() > bottom) isDragDown
                else isDragStop
            }
        }
    }

    private fun performHaptic() = haptic.performHapticFeedback(HapticFeedbackType.LongPress)

    fun dragSelectStart(touchOffset: Offset, onLongClick: (SubCategory) -> Unit) {
        lazyListState.getSelectedItem({ visibleItem ->
            isSelectedItem(touchOffset = touchOffset, visibleItem = visibleItem)
        }) { selectedItem, selectedSubCategory ->
            initialSelectedKeyState = selectedItem.key as? String
            initialSelectedTopState = selectedItem.offset
            initialSelectedBottomState = selectedItem.offsetEnd
            onLongClick(selectedSubCategory)
            performHaptic()
        }
    }

    fun onDrag(touchOffset: Offset, onLongClick: (SubCategory) -> Unit) {
        initialSelectedKeyState?.let { initialSelectedKey ->
            lazyListState.getSelectedItem({ initialSelectedKey != it.key as? String },
                { !passedItemKeys.contains(it.key) },
                { isSelectedItem(touchOffset = touchOffset, visibleItem = it) }
            ) { _, selectedCategory ->
                onLongClick(selectedCategory)
                passedItemKeys.add(selectedCategory.key)
            }
        }
    }

    private fun isSelectedItem(touchOffset: Offset, visibleItem: LazyListItemInfo) =
        touchOffset.y.toInt() in visibleItem.offset..visibleItem.offsetEnd

    fun onDragEndMove(touchOffset: Offset, onLongClick: (SubCategory) -> Unit) {
        when (currentDragPosition(touchOffset)) {
            isDragDown -> dragEndMove(onSelect = onLongClick) { touchOffset.y.toInt() < it.offset }
            isDragUp -> dragEndMove(onSelect = onLongClick) { touchOffset.y.toInt() > it.offsetEnd }
        }
    }

    private fun dragEndMove(
        onSelect: (SubCategory) -> Unit, condition: (selectedItem: LazyListItemInfo) -> Boolean
    ) = lazyListState.getSelectedItem({
        it.key == passedItemKeys.lastOrNull()
    }) { selectedItem, selectedSubCategory ->
        if (condition(selectedItem)) {
            onSelect(selectedSubCategory)
            passedItemKeys.remove(selectedSubCategory.key)
        }
    }

    fun onDragEnd() {
        initialSelectedKeyState = null
        initialSelectedTopState = null
        initialSelectedBottomState = null
        passedItemKeys.clear()
    }

    private fun LazyListState.getSelectedItem(
        vararg conditions: (visibleItem: LazyListItemInfo) -> Boolean,
        task: (LazyListItemInfo, SubCategory) -> Unit
    ) {
        val selectedItem = layoutInfo.visibleItemsInfo.firstOrNull { visibleItem ->
            conditions.forEach { condition ->
                if (!condition(visibleItem)) return@firstOrNull false
            }
            true
        }
        val selectedSubCategory = subCategories.firstOrNull { it.key == selectedItem?.key }

        if (selectedItem != null && selectedSubCategory != null) task(
            selectedItem, selectedSubCategory
        )
    }
}

@Composable
fun rememberSubCategoryContentState(
    selectModeTransition: Transition<Boolean>,
    subCategories: () -> List<SubCategory>,
    lazyListState: LazyListState = rememberLazyListState(),
    haptic: HapticFeedback = LocalHapticFeedback.current,
    interactionSource: InteractionSource = remember { MutableInteractionSource() }
) = remember(subCategories) {
    SubCategoryContentState(
        subCategories = subCategories(),
        lazyListState = lazyListState,
        haptic = haptic,
        interactionSource = interactionSource,
        selectModeTransition = selectModeTransition
    )
}

val LazyListItemInfo.offsetEnd: Int
    get() = offset + size