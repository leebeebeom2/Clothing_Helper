package com.leebeebeom.clothinghelper.ui.main.root.contents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.composable.SingleLineText
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.ui.main.root.components.DrawerExpandIcon
import com.leebeebeom.clothinghelper.ui.main.root.components.DrawerItems
import com.leebeebeom.clothinghelper.ui.main.root.components.DrawerRow
import com.leebeebeom.clothinghelper.ui.main.root.contents.dropdownmenus.DrawerMainCategoryDropDownMenu
import com.leebeebeom.clothinghelper.ui.main.root.model.MainCategory
import com.leebeebeom.clothinghelper.util.AddFolder
import com.leebeebeom.clothinghelper.util.AddSubCategory
import com.leebeebeom.clothinghelper.util.EditFolder
import com.leebeebeom.clothinghelper.util.EditSubCategory
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerMainCategory(
    mainCategory: MainCategory,
    isLoading: () -> Boolean,
    subCategories: (parentName: String) -> ImmutableList<StableSubCategory>,
    folders: (parentKey: String) -> ImmutableList<StableFolder>,
    onClick: (SubCategoryParent) -> Unit,
    onSubCategoryClick: (StableSubCategory) -> Unit,
    onFolderClick: (StableFolder) -> Unit,
    onAddSubCategoryPositiveClick: AddSubCategory,
    onEditSubCategoryPositiveClick: EditSubCategory,
    onAddFolderPositiveClick: AddFolder,
    onEditFolderPositiveClick: EditFolder
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var showDropdownMenu by rememberSaveable { mutableStateOf(false) }

    Column {
        DrawerRow(
            modifier = Modifier.heightIn(44.dp),
            onClick = { onClick(mainCategory.type) },
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
                    subCategories = { subCategories(mainCategory.type.name) },
                    onAddSubCategoryPositiveClick = {
                        onAddSubCategoryPositiveClick(it, mainCategory.type)
                        isExpanded = true
                    })
                Count(items = { subCategories(mainCategory.type.name) }, isLoading = isLoading)
            }
            DrawerExpandIcon(
                isLoading = isLoading,
                isExpanded = { isExpanded },
                onClick = { isExpanded = !isExpanded },
                items = { subCategories(mainCategory.type.name) }
            )
        }
        DrawerItems(
            show = { isExpanded },
            items = { subCategories(mainCategory.type.name) },
            background = MaterialTheme.colors.primary
        ) {
            DrawerSubCategory(
                subCategory = { it },
                subCategories = { subCategories(mainCategory.type.name) },
                folders = { folders(it.key) },
                allFolders = folders,
                onClick = { onSubCategoryClick(it) },
                onFolderClick = onFolderClick,
                onEditSubCategoryNamePositiveClick = onEditSubCategoryPositiveClick,
                onAddFolderPositiveClick = onAddFolderPositiveClick,
                onEditFolderPositiveClick = onEditFolderPositiveClick
            )
        }
    }
}

@Composable
private fun Count(
    items: () -> ImmutableList<*>,
    isLoading: () -> Boolean,
) {
    SingleLineText(
        modifier = Modifier.padding(start = 4.dp),
        text = if (isLoading()) "" else "(${items().size})",
        style = MaterialTheme.typography.caption.copy(
            LocalContentColor.current.copy(ContentAlpha.disabled)
        )
    )
}