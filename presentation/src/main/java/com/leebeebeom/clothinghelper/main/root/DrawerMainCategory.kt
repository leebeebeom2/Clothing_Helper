package com.leebeebeom.clothinghelper.main.root

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
    state: State<DrawerMainCategoryState>,
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (key: String) -> Unit,
) {
    val isExpandState = isExpandStateWithIsAllExpand(isAllExpand = state.value.isAllExpand)

    Column {
        DrawerContentRow(
            modifier = Modifier.heightIn(44.dp),
            onClick = { onMainCategoryClick(state.value.mainCategory.type) }) {
            DrawerContentText(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = state.value.mainCategory.name),
                style = MaterialTheme.typography.subtitle1
            )
            ExpandIcon(
                isLoading = state.value.isLoading,
                isExpand = isExpandState.value,
                onClick = { isExpandState.value = !isExpandState.value }
            )
        }
        SubCategories(
            isExpand = isExpandState.value,
            subCategories = state.value.subCategories,
            onClick = onSubCategoryClick
        )
    }
}

data class DrawerMainCategoryState(
    val mainCategory: MainCategory,
    val subCategories: List<SubCategory>,
    val isLoading: Boolean,
    val isAllExpand: Boolean
)

@Composable
fun rememberDrawerMainCategoryState(
    mainCategory: MainCategory,
    drawerContentsState: State<DrawerContentsState>
) = remember {
    derivedStateOf {
        DrawerMainCategoryState(
            mainCategory = mainCategory,
            subCategories = drawerContentsState.value.allSubCategories[mainCategory.type.ordinal],
            isLoading = drawerContentsState.value.isLoading,
            isAllExpand = drawerContentsState.value.isAllExpand,
        )
    }
}

@Composable
private fun ExpandIcon(
    isLoading: Boolean,
    isExpand: Boolean,
    onClick: () -> Unit
) {
    if (isLoading)
        DotProgressIndicator(
            modifier = Modifier.padding(end = 4.dp),
            size = 4.dp,
            color = MaterialTheme.colors.surface.copy(ContentAlpha.disabled)
        )
    else ExpandIcon(
        isExpanded = isExpand,
        onClick = onClick
    )
}

@Composable
private fun SubCategories(
    isExpand: Boolean,
    subCategories: List<SubCategory>,
    onClick: (key: String) -> Unit
) {
    AnimatedVisibility(
        visible = isExpand,
        enter = listExpand,
        exit = listShrink
    ) {
        Surface(color = MaterialTheme.colors.primary) {
            Column {
                for (subCategory in subCategories)
                    key(subCategory.key) {
                        SubCategory(subCategory = subCategory) { onClick(subCategory.key) }
                    }
            }
        }
    }
}

@Composable
private fun SubCategory(subCategory: SubCategory, onSubCategoryClick: () -> Unit) {
    DrawerContentRow(
        modifier = Modifier
            .heightIn(40.dp)
            .padding(horizontal = 8.dp),
        onClick = onSubCategoryClick
    ) {
        DrawerContentText(
            modifier = Modifier.padding(start = 12.dp),
            text = subCategory.name,
            style = MaterialTheme.typography.subtitle2
        )
    }
}