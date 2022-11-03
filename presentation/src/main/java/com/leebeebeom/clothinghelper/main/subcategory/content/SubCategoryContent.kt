package com.leebeebeom.clothinghelper.main.subcategory.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.main.subcategory.SubCategoryStateHolder
import com.leebeebeom.clothinghelper.main.subcategory.SubCategoryUIState
import com.leebeebeom.clothinghelper.main.subcategory.content.header.SubCategoryHeader
import com.leebeebeom.clothinghelper.main.subcategory.content.header.rememberSubCategoryHeaderState
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.SortOrder
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySort
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySortPreferences

@Composable
fun SubCategoryContent(
    state: SubCategoryContentState,
    paddingValues: PaddingValues,
    allExpandIconClick: () -> Unit,
    onLongClick: (SubCategory) -> Unit,
    onSubCategoryClick: (SubCategory) -> Unit,
    onSortClick: (SubCategorySort) -> Unit,
    onOrderClick: (SortOrder) -> Unit,
    onAddCategoryPositiveButtonClick: (String, SubCategoryParent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, top = 16.dp, bottom = 120.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                val subCategoryHeaderState by rememberSubCategoryHeaderState(subCategoryContentState = state)
                SubCategoryHeader(
                    state = subCategoryHeaderState,
                    allExpandIconClick = allExpandIconClick,
                    onSortClick = onSortClick,
                    onOrderClick = onOrderClick
                )
            }
            items(items = state.subCategoriesState,
                key = { subCategory -> subCategory.key }) {
                val subCategoryCardState by rememberSubCategoryCardState(
                    subCategory = it,
                    subCategoryContentState = state,
                )
                SubCategoryCard(
                    state = subCategoryCardState,
                    onLongClick = { onLongClick(it) },
                    onClick = { onSubCategoryClick(it) },
                )
            }
        }

        AddCategoryDialogFab(
            onPositiveButtonClick = onAddCategoryPositiveButtonClick,
            subCategoriesState = state.subCategoriesState,
            subCategoryParent = state.parent
        )
    }
}

data class SubCategoryContentState(
    val parent: SubCategoryParent,
    val isAllExpand: Boolean,
    val subCategoriesState: List<SubCategory>,
    val isSelectMode: Boolean,
    val selectedSubCategories: Set<SubCategory>,
    val sort: SubCategorySortPreferences,
)

@Composable
fun rememberSubCategoryContentState(
    parent: SubCategoryParent,
    uiState: SubCategoryUIState,
    subCategoriesState: List<SubCategory>,
    SubCategoryStateHolder: SubCategoryStateHolder
) = remember(SubCategoryStateHolder) {
    derivedStateOf {
        SubCategoryContentState(
            parent = parent,
            isAllExpand = uiState.isAllExpand,
            subCategoriesState = subCategoriesState,
            isSelectMode = SubCategoryStateHolder.isSelectModeState,
            selectedSubCategories = SubCategoryStateHolder.selectedSubCategoriesState,
            sort = uiState.sort
        )
    }
}