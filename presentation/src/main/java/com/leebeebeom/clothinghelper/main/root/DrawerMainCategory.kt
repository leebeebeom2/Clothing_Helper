package com.leebeebeom.clothinghelper.main.root

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.Anime.ListExpand.listExpand
import com.leebeebeom.clothinghelper.base.Anime.ListExpand.listShrink
import com.leebeebeom.clothinghelper.base.DotProgressIndicator
import com.leebeebeom.clothinghelper.main.base.AllExpandState
import com.leebeebeom.clothinghelper.main.subcategory.ExpandIcon
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

@Composable
fun DrawerMainCategory(
    drawerMainCategoryState: DrawerMainCategoryState,
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (key: String) -> Unit,
) {
    Column {
        DrawerContentRow(
            modifier = Modifier.heightIn(44.dp),
            onDrawerContentClick = { onMainCategoryClick(drawerMainCategoryState.mainCategory.type) }) {
            DrawerContentText(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = drawerMainCategoryState.mainCategory.name),
                style = MaterialTheme.typography.subtitle1
            )
            DrawerMainCategoryExpandIcon(
                isLoading = drawerMainCategoryState.isLoading,
                isExpand = drawerMainCategoryState.isExpand,
                onExpandIconClick = drawerMainCategoryState::isExpandToggle
            )
        }
        SubCategories(
            isExpand = drawerMainCategoryState.isExpand,
            subCategories = drawerMainCategoryState.subCategories,
            onSubCategoryClick = onSubCategoryClick
        )
    }
}

data class DrawerMainCategoryState(
    val mainCategory: MainCategory,
    val subCategories: List<SubCategory>,
    val isLoading: Boolean,
    override val isAllExpand: Boolean,
    override val _isExpand: MutableState<Boolean>,
    override var rememberedIsAllExpand: Boolean
) : AllExpandState() {
    init {
        init()
    }
}

@Composable
fun rememberDrawerMainCategoryState(
    mainCategory: MainCategory,
    drawerContentsState: DrawerContentsState,
    isExpand: MutableState<Boolean> = rememberSaveable { mutableStateOf(drawerContentsState.isAllExpand) },
    rememberedIsAllExpand: Boolean = rememberSaveable { drawerContentsState.isAllExpand }
) = remember {
    derivedStateOf {
        DrawerMainCategoryState(
            mainCategory = mainCategory,
            subCategories = drawerContentsState.allSubCategories[mainCategory.type.ordinal],
            isLoading = drawerContentsState.isLoading,
            isAllExpand = drawerContentsState.isAllExpand,
            _isExpand = isExpand,
            rememberedIsAllExpand = rememberedIsAllExpand
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
        enter = listExpand,
        exit = listShrink
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