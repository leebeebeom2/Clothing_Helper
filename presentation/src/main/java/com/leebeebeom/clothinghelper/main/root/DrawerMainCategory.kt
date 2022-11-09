package com.leebeebeom.clothinghelper.main.root

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.setValue
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
    subCategories: () -> List<SubCategory>,
    isLoading: () -> Boolean,
    isAllExpand: () -> Boolean,
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (SubCategory) -> Unit,
) {
    var isExpand by isExpandStateWithIsAllExpand(isAllExpand)

    Column {
        DrawerContentRow(
            modifier = Modifier.heightIn(44.dp),
            onClick = { onMainCategoryClick(mainCategory.type) }) {
            DrawerContentText(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = mainCategory.name),
                style = MaterialTheme.typography.subtitle1
            )
            TotalCount(subCategories = subCategories)
            ExpandIcon(
                isLoading = isLoading,
                isExpand = { isExpand },
                onClick = { isExpand = !isExpand }
            )
        }
        SubCategories(
            isExpand = { isExpand },
            subCategories = subCategories,
            onClick = onSubCategoryClick
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
    subCategories: () -> List<SubCategory>,
    onClick: (SubCategory) -> Unit
) = AnimatedVisibility(
    visible = isExpand(),
    enter = listExpand,
    exit = listShrink
) {
    Surface(color = MaterialTheme.colors.primary) {
        Column {
            for (subCategory in subCategories())
                key(subCategory.key) {
                    SubCategory(name = { subCategory.name }, onClick = { onClick(subCategory) })
                }
        }
    }
}

@Composable
private fun SubCategory(name: () -> String, onClick: () -> Unit) {
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
private fun RowScope.TotalCount(subCategories: () -> List<SubCategory>) =
    Text(
        modifier = Modifier
            .weight(1f)
            .padding(start = 4.dp),
        text = "(${subCategories().size})",
        style = MaterialTheme.typography.caption.copy(
            LocalContentColor.current.copy(
                ContentAlpha.disabled
            )
        )
    )