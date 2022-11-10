package com.leebeebeom.clothinghelper.main.root

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.Anime.List.listExpand
import com.leebeebeom.clothinghelper.base.Anime.List.listShrink
import com.leebeebeom.clothinghelper.base.DotProgressIndicator
import com.leebeebeom.clothinghelper.main.base.ExpandIcon
import com.leebeebeom.clothinghelper.main.base.isExpandStateWithIsAllExpand
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

@Composable
fun DrawerMainCategory(
    mainCategory: MainCategory,
    subCategories: (SubCategoryParent) -> List<SubCategory>,
    isLoading: () -> Boolean,
    isAllExpand: () -> Boolean,
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (SubCategory) -> Unit,
    onDrawerClose: () -> Unit,
) {
    var isExpand by isExpandStateWithIsAllExpand(isAllExpand)
    val onRowClick = remember {
        {
            onMainCategoryClick(mainCategory.type)
            onDrawerClose()
        }
    }
    val onExpandIconClick = remember {
        {
            isExpand = !isExpand
        }
    }

    Column {
        DrawerContentRow(
            modifier = Modifier.heightIn(44.dp),
            onClick = onRowClick
        ) {
            DrawerContentText(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = mainCategory.name),
                style = MaterialTheme.typography.subtitle1
            )
            TotalCount(
                mainCategory = mainCategory,
                subCategories = subCategories,
                isLoading = isLoading
            )
            ExpandIcon(
                isLoading = isLoading,
                isExpand = { isExpand },
                onClick = onExpandIconClick
            )
        }
        SubCategories(
            isExpand = { isExpand },
            subCategories = subCategories,
            onClick = onSubCategoryClick,
            mainCategory = mainCategory
        )
    }
}

@Composable
private fun ExpandIcon(
    isLoading: () -> Boolean,
    isExpand: () -> Boolean,
    onClick: () -> Unit
) {
    if (isLoading())
        DotProgressIndicator(
            modifier = Modifier.padding(end = 4.dp),
            size = 4.dp,
            color = MaterialTheme.colors.surface.copy(ContentAlpha.disabled)
        )
    else ExpandIcon(isExpanded = isExpand, onClick = onClick)
}

@Composable
private fun SubCategories(
    isExpand: () -> Boolean,
    subCategories: (SubCategoryParent) -> List<SubCategory>,
    onClick: (SubCategory) -> Unit,
    mainCategory: MainCategory
) {
    AnimatedVisibility(
        visible = isExpand(),
        enter = listExpand,
        exit = listShrink
    ) {
        Surface(color = MaterialTheme.colors.primary) {
            Column {
                for (subCategory in subCategories(mainCategory.type))
                    key(subCategory.key) {
                        SubCategory(subCategory = { subCategory }, onClick = onClick)
                    }
            }
        }
    }
}

@Composable
private fun SubCategory(subCategory: () -> SubCategory, onClick: (SubCategory) -> Unit) {
    val onRowClick = remember {
        {
            onClick(subCategory())
        }
    }
    val name by remember { derivedStateOf { subCategory().name } }
    DrawerContentRow(
        modifier = Modifier
            .heightIn(40.dp)
            .padding(horizontal = 8.dp),
        onClick = onRowClick
    ) {
        DrawerContentText(
            modifier = Modifier.padding(start = 12.dp),
            text = { name },
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
private fun RowScope.TotalCount(
    subCategories: (SubCategoryParent) -> List<SubCategory>,
    isLoading: () -> Boolean,
    mainCategory: MainCategory
) {
    Text(
        modifier = Modifier
            .weight(1f)
            .padding(start = 4.dp),
        text = if (isLoading()) "" else "(${subCategories(mainCategory.type).size})",
        style = MaterialTheme.typography.caption.copy(
            LocalContentColor.current.copy(
                ContentAlpha.disabled
            )
        )
    )
}