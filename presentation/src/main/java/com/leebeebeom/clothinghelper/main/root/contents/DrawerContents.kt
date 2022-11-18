package com.leebeebeom.clothinghelper.main.root.contents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.main.root.DrawerHeader
import com.leebeebeom.clothinghelper.main.root.DrawerMainCategory
import com.leebeebeom.clothinghelper.main.root.model.*
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.map.StableUser
import com.leebeebeom.clothinghelper.theme.Disabled
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerContents(
    user: () -> StableUser?,
    isLoading: () -> Boolean,
    subCategories: (SubCategoryParent) -> ImmutableList<StableSubCategory>,
    subCategoriesSize: (SubCategoryParent) -> Int,
    state: DrawerContentsState = remember { DrawerContentsState() },
    onEssentialMenuClick: (essentialMenu: EssentialMenus) -> Unit,
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (StableSubCategory) -> Unit,
    onSettingIconClick: () -> Unit,
    subCategoryNames: (SubCategoryParent) -> ImmutableList<String>,
    onAddSubCategoryPositiveButtonClick: (StableSubCategory) -> Unit,
    onEditSUbCategoryNamePositiveClick: (StableSubCategory) -> Unit
) {
    Column {
        DrawerHeader(user = user, onSettingIconClick = onSettingIconClick)

        Surface(color = Color(0xFF121212)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 8.dp, bottom = 40.dp)
            ) {
                items(state.essentialMenus, key = { it.name }) {
                    EssentialMenu(
                        essentialMenu = it,
                        onClick = onEssentialMenuClick,
                    )
                }

                item { Divider(color = Disabled, modifier = Modifier.weight(1f)) }

                items(state.mainCategories, key = { it.name }) {
                    DrawerMainCategory(
                        mainCategory = it,
                        subCategories = subCategories,
                        isLoading = isLoading,
                        onMainCategoryClick = onMainCategoryClick,
                        onSubCategoryClick = onSubCategoryClick,
                        subCategoriesSize = subCategoriesSize,
                        subCategoryNames = subCategoryNames,
                        onAddSubCategoryPositiveClick = onAddSubCategoryPositiveButtonClick,
                        onEditSubCategoryNamePositiveClick = onEditSUbCategoryNamePositiveClick
                    )
                }
            }
        }
    }
}

data class DrawerContentsState(
    val essentialMenus: ImmutableList<EssentialMenu> = getEssentialMenus(),
    val mainCategories: ImmutableList<MainCategory> = getMainCategories()
)