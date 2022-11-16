package com.leebeebeom.clothinghelper.main.root

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.Anime.List.listExpand
import com.leebeebeom.clothinghelper.base.Anime.List.listShrink
import com.leebeebeom.clothinghelper.base.DotProgressIndicator
import com.leebeebeom.clothinghelper.base.SingleLineText
import com.leebeebeom.clothinghelper.main.base.ExpandIcon
import com.leebeebeom.clothinghelper.main.root.dropmenus.DrawerMainCategoryDropDownMenu
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerMainCategory(
    mainCategory: MainCategory,
    subCategories: (SubCategoryParent) -> ImmutableList<StableSubCategory>,
    subCategoriesSize: (SubCategoryParent) -> Int,
    isLoading: () -> Boolean,
    isAllExpanded: () -> Boolean,
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (StableSubCategory) -> Unit,
    subCategoryNames: (SubCategoryParent) -> ImmutableList<String>,
    onAddSubCategoryPositiveClick: (StableSubCategory) -> Unit,
    onEditSubCategoryNamePositiveClick: (StableSubCategory) -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var showMainCategoryDropdownMenu by rememberSaveable { mutableStateOf(false) }

    Column {
        DrawerContentRow(
            modifier = Modifier
                .heightIn(44.dp),
            onClick = { onMainCategoryClick(mainCategory.type) },
            onLongClick = { showMainCategoryDropdownMenu = true }
        ) {
            DrawerContentText(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = mainCategory.name),
                style = MaterialTheme.typography.subtitle1
            )
            TotalCount(
                subCategoriesSize = { subCategoriesSize(mainCategory.type) },
                isLoading = isLoading
            )
            DrawerMainCategoryDropDownMenu(
                show = { showMainCategoryDropdownMenu },
                onDismiss = { showMainCategoryDropdownMenu = false },
                subCategoryNames = { subCategoryNames(mainCategory.type) },
                onAddSubCategoryPositiveClick = {
                    onAddSubCategoryPositiveClick(
                        StableSubCategory(
                            parent = mainCategory.type,
                            name = it
                        )
                    )
                }
            )
            ExpandIcon(
                isLoading = isLoading,
                isExpanded = { isExpanded },
                isAllExpanded = isAllExpanded,
                onClick = { isExpanded = !isExpanded },
                subCategoriesSize = { subCategoriesSize(mainCategory.type) },
                updateIsExpand = { isExpanded = it }
            )
        }
        SubCategories(
            isExpanded = { isExpanded },
            subCategories = { subCategories(mainCategory.type) },
            onClick = onSubCategoryClick,
            onEditSubCategoryNamePositiveClick = onEditSubCategoryNamePositiveClick,
            subCategoryNames = { subCategoryNames(mainCategory.type) }
        )
    }
}

@Composable
private fun ExpandIcon(
    isLoading: () -> Boolean,
    isExpanded: () -> Boolean,
    onClick: () -> Unit,
    isAllExpanded: () -> Boolean,
    subCategoriesSize: () -> Int,
    updateIsExpand: (Boolean) -> Unit
) {
    if (isLoading())
        DotProgressIndicator(
            modifier = Modifier.padding(end = 4.dp),
            size = 4.dp,
            color = MaterialTheme.colors.surface.copy(ContentAlpha.disabled)
        )
    else if (subCategoriesSize() > 0) ExpandIcon(
        isExpanded = isExpanded,
        onClick = onClick,
        isAllExpanded = isAllExpanded,
        updateIsExpand = updateIsExpand
    )
}

@Composable
private fun SubCategories(
    isExpanded: () -> Boolean,
    subCategories: () -> ImmutableList<StableSubCategory>,
    onClick: (StableSubCategory) -> Unit,
    onEditSubCategoryNamePositiveClick: (StableSubCategory) -> Unit,
    subCategoryNames: () -> ImmutableList<String>
) {
    AnimatedVisibility(
        visible = isExpanded(),
        enter = listExpand,
        exit = listShrink
    ) {
        SubCategories(
            subCategories = subCategories,
            onClick = onClick,
            onEditSubCategoryNamePositiveClick = onEditSubCategoryNamePositiveClick,
            subCategoryNames = subCategoryNames
        )
    }
}

@Composable
private fun SubCategories(
    subCategories: () -> ImmutableList<StableSubCategory>,
    onClick: (StableSubCategory) -> Unit,
    onEditSubCategoryNamePositiveClick: (StableSubCategory) -> Unit,
    subCategoryNames: () -> ImmutableList<String>
) {
    Surface(color = MaterialTheme.colors.primary) {
        Column {
            for (subCategory in subCategories())
                key(subCategory.key) {
                    DrawerSubCategory(
                        subCategory = { subCategory },
                        onClick = { onClick(subCategory) },
                        onEditSubCategoryNamePositiveClick = onEditSubCategoryNamePositiveClick,
                        subCategoryNames = subCategoryNames
                    )
                }
        }
    }
}

@Composable
private fun RowScope.TotalCount(
    subCategoriesSize: () -> Int,
    isLoading: () -> Boolean,
) {
    SingleLineText(
        modifier = Modifier
            .weight(1f)
            .padding(start = 4.dp),
        text = if (isLoading()) "" else "(${subCategoriesSize()})",
        style = MaterialTheme.typography.caption.copy(
            LocalContentColor.current.copy(
                ContentAlpha.disabled
            )
        )
    )
}