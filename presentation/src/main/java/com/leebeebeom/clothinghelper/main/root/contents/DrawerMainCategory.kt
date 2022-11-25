package com.leebeebeom.clothinghelper.main.root.contents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.main.root.components.DrawerContentRow
import com.leebeebeom.clothinghelper.main.root.components.DrawerExpandIcon
import com.leebeebeom.clothinghelper.main.root.components.DrawerItems
import com.leebeebeom.clothinghelper.main.root.components.DrawerMainTotalCount
import com.leebeebeom.clothinghelper.main.root.contents.dropdownmenus.DrawerMainCategoryDropDownMenu
import com.leebeebeom.clothinghelper.main.root.model.MainCategory
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerMainCategory(
    mainCategory: MainCategory,
    subCategories: (SubCategoryParent) -> ImmutableList<StableSubCategory>,
    isLoading: () -> Boolean,
    onMainCategoryClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (StableSubCategory) -> Unit,
    onAddSubCategoryPositiveClick: (StableSubCategory) -> Unit,
    onEditSubCategoryPositiveClick: (StableSubCategory) -> Unit,
    onAddFolderPositiveClick: (StableFolder) -> Unit,
    folders: (parentKey: String) -> ImmutableList<StableFolder>,
    onFolderClick: (StableFolder) -> Unit,
    onEditFolderPositiveClick: (StableFolder) -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var showDropdownMenu by rememberSaveable { mutableStateOf(false) }

    Column {
        DrawerContentRow(
            modifier = Modifier.heightIn(44.dp),
            onClick = { onMainCategoryClick(mainCategory.type) },
            onLongClick = { showDropdownMenu = true }
        ) {
            Row(modifier = Modifier.weight(1f)) {
                SingleLineText(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(id = mainCategory.name),
                    style = MaterialTheme.typography.body1
                )
                DrawerMainCategoryDropDownMenu(
                    show = { showDropdownMenu },
                    onDismiss = { showDropdownMenu = false },
                    subCategories = { subCategories(mainCategory.type) },
                    onAddSubCategoryPositiveClick = {
                        onAddSubCategoryPositiveClick(
                            StableSubCategory(
                                parent = mainCategory.type,
                                name = it
                            )
                        )
                    }
                )
            }
            DrawerMainTotalCount(
                items = { subCategories(mainCategory.type) },
                isLoading = isLoading
            )
            DrawerExpandIcon(
                isLoading = isLoading,
                isExpanded = { isExpanded },
                onClick = { isExpanded = !isExpanded },
                items = { subCategories(mainCategory.type) }
            )
        }
        DrawerItems(
            show = { isExpanded },
            items = { subCategories(mainCategory.type) },
            backGround = MaterialTheme.colors.primary
        ) {
            DrawerSubCategory(
                subCategory = { it },
                onClick = { onSubCategoryClick(it) },
                onEditSubCategoryNamePositiveClick = onEditSubCategoryPositiveClick,
                subCategories = { subCategories(mainCategory.type) },
                onAddFolderPositiveClick = onAddFolderPositiveClick,
                folders = folders,
                onFolderClick = onFolderClick,
                onEditFolderPositiveClick = onEditFolderPositiveClick
            )
        }
    }
}