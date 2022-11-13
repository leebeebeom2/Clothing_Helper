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
    var initialSelectedIndex: Int? = null,
    var initialSelectedTop: Int? = null,
    var initialSelectedBottom: Int? = null,
    var lastSelectedIndex: Int? = null,
    var lastSelectedTop: Int? = null,
    var lastSelectedBottom: Int? = null,
    var dragDirection: DragDirection = DragDirection.None,
    val passedItemKeys: LinkedHashSet<String> = linkedSetOf(),
    val interactionSource: InteractionSource,
    val configuration: Configuration,
    val coroutineScope: CoroutineScope
) {
    enum class DragDirection {
        Up, Down, None
    }

    private val scrollAnimationSpec = tween<Float>(300, easing = FastOutLinearInEasing)

    val showScrollToTopButton by derivedStateOf { lazyListState.firstVisibleItemScrollOffset > 0 }

    fun scrollToTop() {
        coroutineScope.launch {
            val firstVisibleItemIndex =
                lazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.index
            firstVisibleItemIndex?.also {
                if (it <= 2) lazyListState.animateScrollBy(-1000f, scrollAnimationSpec)
                else {
                    lazyListState.scrollToItem(2)
                    lazyListState.animateScrollBy(-1000f, scrollAnimationSpec)
                }
            }
        }
    }

    fun performHaptic() {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    inline fun dragSelectStart(
        touchOffset: Offset, crossinline onLongClick: (key: String) -> Unit
    ) {
        lazyListState.getSelectedItem(touchOffset) { selectedItem ->
            (selectedItem.key as? String)?.let {
                initialSelectedIndex = selectedItem.index
                initialSelectedTop = selectedItem.realOffset
                initialSelectedBottom = selectedItem.offsetEnd
                passedItemKeys.add(it)
                onLongClick(it)
                performHaptic()
            }
        }
    }

    inline fun onDrag(touchOffset: Offset, crossinline onSelect: (String) -> Unit) {
        initialSelectedIndex?.let { index ->
            initialSelectedTop?.let { top ->
                initialSelectedBottom?.let { bottom ->
                    dragDirection = getDragDirection(top, bottom, touchOffset)

                    if (dragDirection == DragDirection.Down)
                        onDragStart(
                            touchOffset = touchOffset,
                            range = { index..it },
                            passedItemKeysContainsOrNot = { !passedItemKeys.contains(it) },
                            onSelect = onSelect,
                            passedItemKeysAddOrRemove = passedItemKeys::addAll,
                            selectedItemFirstOrLast = { it.lastOrNull() }
                        )
                    else if (dragDirection == DragDirection.Up)
                        onDragStart(
                            touchOffset = touchOffset,
                            range = { it..index },
                            passedItemKeysContainsOrNot = { !passedItemKeys.contains(it) },
                            onSelect = onSelect,
                            passedItemKeysAddOrRemove = passedItemKeys::addAll,
                            selectedItemFirstOrLast = { it.firstOrNull() }
                        )
                }
            }
        }

    }

    inline fun onDragEndMove(touchOffset: Offset, crossinline onSelect: (key: String) -> Unit) {
        lastSelectedIndex?.let { index ->
            lastSelectedTop?.let { top ->
                lastSelectedBottom?.let { bottom ->
                    val dragDirection = getDragEndDirection(top, bottom, touchOffset)
                    println(dragDirection)
                    if (dragDirection == DragDirection.Up)
                        onDragStart(
                            touchOffset = touchOffset,
                            range = { it..index },
                            passedItemKeysContainsOrNot = passedItemKeys::contains,
                            onSelect = onSelect,
                            passedItemKeysAddOrRemove = passedItemKeys::removeAll,
                            selectedItemFirstOrLast = { it.lastOrNull() }
                        )
                    else if (dragDirection == DragDirection.Down)
                        onDragStart(
                            touchOffset = touchOffset,
                            range = { index..it },
                            passedItemKeysContainsOrNot = passedItemKeys::contains,
                            onSelect = onSelect,
                            passedItemKeysAddOrRemove = passedItemKeys::removeAll,
                            selectedItemFirstOrLast = { it.firstOrNull() }
                        )
                }
            }
        }
    }

    inline fun onDragStart(
        touchOffset: Offset,
        crossinline range: (selectedItemIndex: Int) -> IntRange,
        crossinline passedItemKeysContainsOrNot: (String) -> Boolean,
        crossinline onSelect: (String) -> Unit,
        crossinline passedItemKeysAddOrRemove: (Set<String>) -> Unit,
        crossinline selectedItemFirstOrLast: (List<LazyListItemInfo>) -> LazyListItemInfo?
    ) {
        lazyListState.getSelectedItem(touchOffset) { selectedItem ->
            val selectedItems = lazyListState.layoutInfo.visibleItemsInfo.filter {
                (it.key as? String)?.let { key ->
                    it.index != initialSelectedIndex &&
                            it.index in range(selectedItem.index) &&
                            passedItemKeysContainsOrNot(key)
                } ?: false
            }
            val selectedKeys = selectedItems.map { it.key as String }
            passedItemKeysAddOrRemove(selectedKeys.toSet())
            selectedKeys.forEach(onSelect)
            selectedItemFirstOrLast(selectedItems)?.let {
                lastSelectedIndex = it.index
                lastSelectedTop = it.realOffset
                lastSelectedBottom = it.offsetEnd
            }
        }
    }

    fun getDragEndDirection(lastTop: Int, lastBottom: Int, touchOffset: Offset): DragDirection {
        val dragEndDirection = getDragDirection(lastTop, lastBottom, touchOffset)
        return if ((dragDirection == DragDirection.Down || dragDirection == DragDirection.None) && dragEndDirection == DragDirection.Up
        ) DragDirection.Up
        else if ((dragDirection == DragDirection.Up || dragDirection == DragDirection.None) && dragEndDirection == DragDirection.Down
        ) DragDirection.Down
        else DragDirection.None
    }

    fun getDragDirection(top: Int, bottom: Int, touchOffset: Offset): DragDirection {
        return if (top > touchOffset.y.toInt()) DragDirection.Up
        else if (bottom < touchOffset.y.toInt()) DragDirection.Down
        else DragDirection.None
    }

    fun onDragEnd() {
        initialSelectedIndex = null
        initialSelectedTop = null
        initialSelectedBottom = null
        lastSelectedIndex = null
        lastSelectedTop = null
        lastSelectedBottom = null
        dragDirection = DragDirection.None
        passedItemKeys.clear()
    }

    inline fun LazyListState.getSelectedItem(
        touchOffset: Offset,
        crossinline task: (LazyListItemInfo) -> Unit
    ) {
        layoutInfo.visibleItemsInfo.firstOrNull {
            touchOffset.y.toInt() in it.realOffset..it.offsetEnd
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