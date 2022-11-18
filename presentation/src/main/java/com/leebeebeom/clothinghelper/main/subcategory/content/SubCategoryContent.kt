package com.leebeebeom.clothinghelper.main.subcategory.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.main.base.components.ScrollToTopFab
import com.leebeebeom.clothinghelper.main.subcategory.content.subcategorycard.SubCategoryCard
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.util.dragSelect
import com.leebeebeom.clothinghelper.util.scrollToTop
import com.leebeebeom.clothinghelper.util.showScrollToTopButton
import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.SortPreferences
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

// TODO 오토스크롤 구현

@Composable
fun SubCategoryContent(
    parent: SubCategoryParent,
    subCategories: () -> ImmutableList<StableSubCategory>,
    sort: () -> SortPreferences,
    state: LazyListState = rememberLazyListState(),
    onLongClick: (key: String) -> Unit,
    onSubCategoryClick: (SubCategoryParent, name: String, key: String) -> Unit,
    onSortClick: (Sort) -> Unit,
    onOrderClick: (Order) -> Unit,
    selectedSubCategoryKey: () -> ImmutableSet<String>,
    isSelectMode: () -> Boolean,
    onSelect: (String) -> Unit
) {
    var dragSelectStart by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .dragSelect(
                    state,
                    { dragSelectStart = it },
                    onSelect,
                    onLongClick,
                    LocalHapticFeedback.current
                ),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, top = 16.dp, bottom = 120.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = state,
            userScrollEnabled = !dragSelectStart
        ) {
            item {
                SubCategoryHeader(
                    parent = parent,
                    sort = sort,
                    onSortClick = onSortClick,
                    onOrderClick = onOrderClick
                )
            }
            items(items = subCategories(), key = { it.key }) {
                SubCategoryCard(subCategory = { it }, onClick = {
                    if (isSelectMode()) onSelect(it.key) else onSubCategoryClick(
                        it.parent,
                        it.name,
                        it.key
                    )
                }, selectedCategoryKeys = selectedSubCategoryKey, isSelectMode = isSelectMode
                )
            }
        }

        ScrollToTopFab(showFab = { state.showScrollToTopButton }, onClick = state::scrollToTop)
    }
}