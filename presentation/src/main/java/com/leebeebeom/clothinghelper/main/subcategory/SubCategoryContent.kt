package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.main.subcategory.header.SubCategoryHeader
import com.leebeebeom.clothinghelper.main.subcategory.header.rememberSubCategoryHeaderState
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.SortOrder
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySort
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySortPreferences

@Composable
fun SubCategoryContent(
    subCategoryContentState: SubCategoryContentState,
    allExpandIconClick: () -> Unit,
    onLongClick: (SubCategory) -> Unit,
    onSubCategoryClick: (SubCategory) -> Unit,
    onSortClick: (SubCategorySort) -> Unit,
    onOrderClick: (SortOrder) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp, end = 16.dp, top = 16.dp, bottom = 120.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            val subCategoryHeaderState by rememberSubCategoryHeaderState(subCategoryContentState = subCategoryContentState)
            SubCategoryHeader(
                subCategoryHeaderState = subCategoryHeaderState,
                allExpandIconClick = allExpandIconClick,
                onSortClick = onSortClick,
                onOrderClick = onOrderClick
            )
        }
        items(items = subCategoryContentState.subCategories,
            key = { subCategory -> subCategory.key }) {
            val subCategoryCardState by rememberSubCategoryCardState(
                subCategory = it,
                subCategoryContentState = subCategoryContentState,
            )
            SubCategoryCard(
                subCategoryCardState = subCategoryCardState,
                onLongClick = { onLongClick(it) },
                onClick = { onSubCategoryClick(it) },
            )
        }
    }
}

fun getHeaderStringRes(mainCategoryName: String) =
    when (mainCategoryName) {
        SubCategoryParent.TOP.name -> R.string.top
        SubCategoryParent.BOTTOM.name -> R.string.bottom
        SubCategoryParent.OUTER.name -> R.string.outer
        else -> R.string.etc
    }

data class SubCategoryContentState(
    @StringRes val headerText: Int,
    val isAllExpand: Boolean,
    val subCategories: List<SubCategory>,
    val isSelectMode: Boolean,
    val selectedSubCategories: Set<SubCategory>,
    val sort: SubCategorySortPreferences,
)

@Composable
fun rememberSubCategoryContentState(
    mainCategoryName: String,
    uiState: SubCategoryUIState,
    subCategories: List<SubCategory>,
    subCategoryScreenState: SubCategoryScreenState
) = remember {
    derivedStateOf {
        SubCategoryContentState(
            headerText = getHeaderStringRes(mainCategoryName),
            isAllExpand = uiState.isAllExpand,
            subCategories = subCategories,
            isSelectMode = subCategoryScreenState.isSelectMode,
            selectedSubCategories = subCategoryScreenState.selectedSubCategories,
            sort = uiState.sort
        )
    }
}