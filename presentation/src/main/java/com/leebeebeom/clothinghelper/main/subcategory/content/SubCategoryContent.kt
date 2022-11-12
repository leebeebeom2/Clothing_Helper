package com.leebeebeom.clothinghelper.main.subcategory.content

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelper.main.subcategory.content.header.SubCategoryHeader
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.SortOrder
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySort
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySortPreferences
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// TODO 모든 애니메이션 0에서 타겟으로 가지말고 특정 높이 혹은 상태에서 타겟으로
// TODO 오토스크롤 구현

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
                    }, onDragCancel = {
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

        ScrollToTopFab(showFab = { state.showScrollToTopButton }, onClick = state::scrollToTop)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun BoxScope.ScrollToTopFab(showFab: () -> Boolean, onClick: () -> Unit) {
    AnimatedVisibility(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(4.dp),
        visible = showFab(),
        enter = scaleIn(
            spring(stiffness = Spring.StiffnessMedium),
            transformOrigin = TransformOrigin(0.5f, 0.5f)
        ),
        exit = scaleOut(
            animationSpec = spring(stiffness = Spring.StiffnessMedium),
            transformOrigin = TransformOrigin(0.5f, 0.5f)
        )
    ) {
        FloatingActionButton(
            onClick = onClick,
            backgroundColor = MaterialTheme.colors.surface,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(36.dp),
            elevation = FloatingActionButtonDefaults.elevation(4.dp, 10.dp, 6.dp, 6.dp)
        ) {
            SimpleIcon(drawable = R.drawable.ic_expand_less)
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
    val interactionSource: InteractionSource,
    val configuration: Configuration,
    val coroutineScope: CoroutineScope
) {
    enum class DragPosition {
        DragUp, DragDown, DragStop
    }

    private val scrollAnimationSpec = tween<Float>(300, easing = FastOutLinearInEasing)

    val showScrollToTopButton by derivedStateOf { lazyListState.firstVisibleItemScrollOffset > 0 }

    fun scrollToTop() {
        coroutineScope.launch {
            val firstVisibleItemIndex =
                lazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.index
            firstVisibleItemIndex?.also {
                if (it < 2) lazyListState.animateScrollBy(-1000f, scrollAnimationSpec)
                else if (it > 2) {
                    lazyListState.scrollToItem(2)
                    lazyListState.animateScrollBy(-1000f, scrollAnimationSpec)
                }
            }
        }
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
    interactionSource: InteractionSource = remember { MutableInteractionSource() },
    configuration: Configuration = LocalConfiguration.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): SubCategoryContentState {
    return remember {
        SubCategoryContentState(
            lazyListState = lazyListState,
            haptic = haptic,
            interactionSource = interactionSource,
            configuration = configuration,
            coroutineScope = coroutineScope
        )
    }
}

val LazyListItemInfo.realOffset: Int
    get() = offset + 50

val LazyListItemInfo.offsetEnd: Int
    get() = offset + size + 43