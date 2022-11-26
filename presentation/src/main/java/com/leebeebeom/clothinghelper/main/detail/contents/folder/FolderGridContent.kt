package com.leebeebeom.clothinghelper.main.detail.contents.folder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.main.base.composables.ScrollToTopFab
import com.leebeebeom.clothinghelper.main.base.composables.sort.SortIconWithDivider
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.util.dragSelect.GridDragSelector
import com.leebeebeom.clothinghelper.util.dragSelect.dragSelect
import com.leebeebeom.clothinghelper.util.scrollToTop
import com.leebeebeom.clothinghelper.util.showScrollToTopFab
import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.SortPreferences
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun FolderGridContent(
    parentKey: String,
    folders: (parentKey: String) -> ImmutableList<StableFolder>,
    selectedFolderKeys: () -> ImmutableSet<String>,
    isSelectMode: () -> Boolean,
    onSelect: (String) -> Unit,
    onLongClick: (String) -> Unit,
    sort: () -> SortPreferences,
    onSortClick: (Sort) -> Unit,
    onOrderClick: (Order) -> Unit,
    onClick: (StableFolder) -> Unit
) {
    val lazyState = rememberLazyGridState()
    val haptic = LocalHapticFeedback.current
    var dragSelectStart by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize()) {
        LazyVerticalGrid(
            state = lazyState,
            modifier = Modifier
                .fillMaxWidth()
                .dragSelect(
                    dragSelector = remember { GridDragSelector(lazyState, haptic) },
                    onDragStart = { dragSelectStart = true },
                    onSelect = onSelect,
                    onLongClick = onLongClick
                ),
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            userScrollEnabled = !dragSelectStart
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                SortIconWithDivider(
                    sort = sort,
                    onSortClick = onSortClick,
                    onOrderClick = onOrderClick
                )
            }

            items(items = folders(parentKey), key = { it.key }) {
                Folder(
                    selectedFolderKeys = selectedFolderKeys,
                    folder = it,
                    isSelectMode = isSelectMode,
                    onSelect = onSelect,
                    onClick = onClick,
                    folders = folders
                )
            }
        }
        ScrollToTopFab(show = { lazyState.showScrollToTopFab }, toTop = lazyState::scrollToTop)
    }
}