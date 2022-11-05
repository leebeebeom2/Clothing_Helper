package com.leebeebeom.clothinghelper.main.subcategory.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.main.subcategory.SubCategoryStates
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
    state: State<SubCategoryContentState>,
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
            .padding(state.value.paddingValues)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, top = 16.dp, bottom = 120.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                val subCategoryHeaderState =
                    rememberSubCategoryHeaderState(subCategoryContentState = state)
                SubCategoryHeader(
                    state = subCategoryHeaderState,
                    allExpandIconClick = allExpandIconClick,
                    onSortClick = onSortClick,
                    onOrderClick = onOrderClick
                )
            }
            items(items = state.value.subCategories,
                key = { subCategory -> subCategory.key }) {
                val subCategoryCardState = rememberSubCategoryCardState(
                    subCategory = it,
                    subCategoryContentState = state,
                )
                SubCategoryCard(
                    state = subCategoryCardState.value,
                    onLongClick = { onLongClick(it) },
                    onClick = { onSubCategoryClick(it) },
                )
            }
        }

        AddSubcategoryDialogFab(
            onPositiveButtonClick = { onAddCategoryPositiveButtonClick(it.trim(), state.value.parent) },
            subCategories = state.value.subCategories,
        )
    }
}

data class SubCategoryContentState(
    val parent: SubCategoryParent,
    val isAllExpand: Boolean,
    val subCategories: List<SubCategory>,
    val isSelectMode: Boolean,
    val selectedSubCategories: Set<SubCategory>,
    val sort: SubCategorySortPreferences,
    val paddingValues: PaddingValues
)

@Composable
fun rememberSubCategoryContentState(
    uiState: State<SubCategoryUIState>,
    subCategoriesState: State<List<SubCategory>>,
    subCategoryStates: SubCategoryStates,
    paddingValues: PaddingValues
) = remember(paddingValues) {
    derivedStateOf {
        SubCategoryContentState(
            parent = subCategoryStates.parent,
            isAllExpand = uiState.value.isAllExpand,
            subCategories = subCategoriesState.value,
            isSelectMode = subCategoryStates.isSelectMode,
            selectedSubCategories = subCategoryStates.selectedSubCategories,
            sort = uiState.value.sort,
            paddingValues = paddingValues
        )
    }
}