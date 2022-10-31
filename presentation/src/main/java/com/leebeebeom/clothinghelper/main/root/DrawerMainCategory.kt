package com.leebeebeom.clothinghelper.main.root

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.DotProgressIndicator
import com.leebeebeom.clothinghelper.main.subcategory.ExpandIcon
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

@Composable
fun DrawerMainCategory(
    mainCategory: MainCategory,
    subCategories: List<SubCategory>,
    isLoading: Boolean,
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (key: String) -> Unit,
) {
    var isExpand by rememberSaveable { mutableStateOf(false) }

    Column {
        DrawerContentRow(
            modifier = Modifier.heightIn(48.dp),
            onDrawerContentClick = { onMainCategoryClick(mainCategory.type) }) {
            DrawerContentText(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = mainCategory.name),
                style = MaterialTheme.typography.subtitle1
            )
            DrawerMainCategoryExpandIcon(
                isLoading = isLoading,
                isExpand = isExpand
            ) { isExpand = !isExpand }
        }
        SubCategories(
            isExpand = isExpand,
            subCategories = subCategories,
            onSubCategoryClick = onSubCategoryClick
        )
    }
}

@Composable
private fun DrawerMainCategoryExpandIcon(
    isLoading: Boolean,
    isExpand: Boolean,
    onExpandIconClick: () -> Unit
) {
    if (isLoading)
        DotProgressIndicator(
            modifier = Modifier.padding(end = 12.dp),
            dotSize = 4.dp,
            color = MaterialTheme.colors.surface.copy(ContentAlpha.disabled)
        )
    else ExpandIcon(
        modifier = Modifier.size(22.dp),
        isExpanded = isExpand,
        onExpandIconClick = onExpandIconClick
    )
}

@Composable
private fun SubCategories(
    isExpand: Boolean,
    subCategories: List<SubCategory>,
    onSubCategoryClick: (key: String) -> Unit
) {
    AnimatedVisibility(
        visible = isExpand,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Surface(color = MaterialTheme.colors.primary) {
            Column {
                for (subCategory in subCategories)
                    key(subCategory.key) {
                        DrawerSubCategory(subCategory = subCategory) {
                            onSubCategoryClick(subCategory.key)
                        }
                    }
            }
        }
    }
}

@Composable
private fun DrawerSubCategory(subCategory: SubCategory, onSubCategoryClick: () -> Unit) {
    DrawerContentRow(
        modifier = Modifier
            .heightIn(40.dp)
            .padding(horizontal = 8.dp),
        onDrawerContentClick = onSubCategoryClick
    ) {
        DrawerContentText(
            modifier = Modifier.padding(start = 12.dp),
            text = subCategory.name,
            style = MaterialTheme.typography.subtitle2
        )
    }
}