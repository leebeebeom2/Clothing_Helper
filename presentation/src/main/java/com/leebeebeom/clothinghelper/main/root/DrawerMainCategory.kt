package com.leebeebeom.clothinghelper.main.root

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.Anime.List.listExpand
import com.leebeebeom.clothinghelper.base.Anime.List.listShrink
import com.leebeebeom.clothinghelper.base.DotProgressIndicator
import com.leebeebeom.clothinghelper.main.base.ExpandIcon
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerMainCategory(
    mainCategory: MainCategory,
    subCategories: (SubCategoryParent) -> ImmutableList<StableSubCategory>,
    subCategoriesSize: (SubCategoryParent) -> Int,
    isLoading: () -> Boolean,
    isAllExpand: () -> Boolean,
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (StableSubCategory) -> Unit,
    subCategoryNames: () -> ImmutableList<String>,
    onAddSubCategoryPositiveClick: (String, SubCategoryParent) -> Unit
) {
    var isExpand by rememberSaveable { mutableStateOf(false) }
    var showDropdownMenu by rememberSaveable { mutableStateOf(false) }

    Column {
        DrawerContentRow(
            modifier = Modifier
                .heightIn(44.dp),
            onClick = { onMainCategoryClick(mainCategory.type) },
            onLongClick = { showDropdownMenu = true }
        ) {
            DrawerContentText(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = mainCategory.name),
                style = MaterialTheme.typography.subtitle1
            )
            DrawerMainCategoryDropDownMenu(
                show = { showDropdownMenu },
                onDismiss = { showDropdownMenu = false },
                subCategoryNames = subCategoryNames,
                onAddSubCategoryPositiveClick = {
                    onAddSubCategoryPositiveClick(it, mainCategory.type)
                }
            )
            TotalCount(
                subCategoriesSize = { subCategoriesSize(mainCategory.type) },
                isLoading = isLoading
            )
            ExpandIcon(
                isLoading = isLoading,
                isExpand = { isExpand },
                isAllExpand = isAllExpand,
                onClick = { isExpand = !isExpand },
                updateIsExpand = { isExpand = it }
            )
        }
        SubCategories(
            isExpand = { isExpand },
            subCategories = { subCategories(mainCategory.type) },
            onClick = onSubCategoryClick
        )
    }
}

@Composable
private fun ExpandIcon(
    isLoading: () -> Boolean,
    isExpand: () -> Boolean,
    onClick: () -> Unit,
    isAllExpand: () -> Boolean,
    updateIsExpand: (Boolean) -> Unit
) {
    if (isLoading())
        DotProgressIndicator(
            modifier = Modifier.padding(end = 4.dp),
            size = 4.dp,
            color = MaterialTheme.colors.surface.copy(ContentAlpha.disabled)
        )
    else ExpandIcon(
        isExpanded = isExpand,
        onClick = onClick,
        isAllExpand = isAllExpand,
        updateIsExpand = updateIsExpand
    )
}

@Composable
private fun SubCategories(
    isExpand: () -> Boolean,
    subCategories: () -> ImmutableList<StableSubCategory>,
    onClick: (StableSubCategory) -> Unit
) {
    AnimatedVisibility(
        visible = isExpand(),
        enter = listExpand,
        exit = listShrink
    ) { SubCategories(subCategories = subCategories, onClick = onClick) }
}

@Composable
private fun SubCategories(
    subCategories: () -> ImmutableList<StableSubCategory>,
    onClick: (StableSubCategory) -> Unit
) {
    Surface(color = MaterialTheme.colors.primary) {
        Column {
            for (subCategory in subCategories())
                key(subCategory.key) {
                    SubCategory(
                        name = { subCategory.name },
                        onClick = { onClick(subCategory) }
                    )
                }
        }
    }
}

@Composable
private fun SubCategory(
    name: () -> String,
    onClick: () -> Unit
) {
    DrawerContentRow(
        modifier = Modifier
            .heightIn(40.dp)
            .padding(horizontal = 8.dp),
        onClick = onClick
    ) {
        DrawerContentText(
            modifier = Modifier.padding(start = 12.dp),
            text = name,
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
private fun RowScope.TotalCount(
    subCategoriesSize: () -> Int,
    isLoading: () -> Boolean,
) {
    Text(
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