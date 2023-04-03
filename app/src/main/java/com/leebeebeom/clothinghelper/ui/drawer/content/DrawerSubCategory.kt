package com.leebeebeom.clothinghelper.ui.drawer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.ui.component.HeightSpacer
import com.leebeebeom.clothinghelper.ui.component.SingleLineText
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemState
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerCount
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerExpandIcon
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerItemsWrapperWithExpandAnimation
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerRow
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerSubCategoryDropDownMenu
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemState
import com.leebeebeom.clothinghelper.ui.theme.DarkGray
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import com.leebeebeom.clothinghelper.ui.util.EditSubCategory
import kotlinx.collections.immutable.*

@Composable
fun DrawerSubCategories(
    filteredSubCategories: () -> ImmutableList<SubCategory>,
    filteredSubCategoryNames: () -> ImmutableSet<String>,
    folders: () -> ImmutableList<Folder>,
    folderNames: () -> ImmutableMap<String, ImmutableSet<String>>,
    folderSize: () -> ImmutableMap<String, Int>,
    itemSize: () -> ImmutableMap<String, Int>,
    onSubCategoryClick: (SubCategory) -> Unit,
    editSubCategory: EditSubCategory,
    addFolder: AddFolder,
    density: Density,
    drawerFolders: @Composable (filteredFolders: () -> ImmutableList<Folder>, filteredFolderNames: () -> ImmutableSet<String>) -> Unit
) {
    Column(modifier = Modifier.background(DarkGray)) {
        filteredSubCategories().forEach { subCategory -> // 여기서 전달된 subCategory는 State가 아니기 때문에 추적이 안됨.
            key(subCategory.key) {
                DrawerSubCategory(
                    folders = folders,
                    subCategory = subCategory, // 람다 사용 시 전체 리컴포즈
                    onSubCategoryClick = onSubCategoryClick,
                    filteredSubCategoryNames = filteredSubCategoryNames,
                    folderNames = folderNames,
                    editSubCategory = editSubCategory,
                    addFolder = addFolder,
                    folderSize = folderSize,
                    itemSize = itemSize,
                    density = density,
                    drawerFolders = drawerFolders
                )
            }
        }
    }
}

@Composable
private fun DrawerSubCategory(
    subCategory: SubCategory,
    onSubCategoryClick: (SubCategory) -> Unit,
    folders: () -> ImmutableList<Folder>,
    filteredSubCategoryNames: () -> ImmutableSet<String>,
    folderNames: () -> ImmutableMap<String, ImmutableSet<String>>,
    editSubCategory: EditSubCategory,
    addFolder: AddFolder,
    folderSize: () -> ImmutableMap<String, Int>,
    itemSize: () -> ImmutableMap<String, Int>,
    state: DrawerItemState = rememberDrawerItemState(),
    density: Density,
    drawerFolders: @Composable (filteredFolders: () -> ImmutableList<Folder>, filteredFolderNames: () -> ImmutableSet<String>) -> Unit
) {
    val childFolderNames by remember {
        derivedStateOf {
            folderNames().getOrDefault(key = subCategory.key, defaultValue = persistentSetOf())
        }
    }
    val childFolderSize by remember {
        derivedStateOf { folderSize().getOrDefault(key = subCategory.key, defaultValue = 0) }
    }
    val childItemSize by remember {
        derivedStateOf { itemSize().getOrDefault(key = subCategory.key, defaultValue = 0) }
    }
    val longClickOffset by remember {
        derivedStateOf {
            with(density) {
                DpOffset(
                    x = state.longClickOffsetX.toDp(),
                    y = state.longClickOffsetY.toDp() - state.itemHeight.toDp()
                )
            }
        }
    }

    DrawerRow(
        modifier = Modifier.padding(start = 8.dp),
        onClick = { onSubCategoryClick(subCategory) },
        onLongClick = state::onLongClick,
        onSizeChange = state::onSizeChanged
    ) {
        Row(
            Modifier.padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                SingleLineText(
                    text = { subCategory.name }, style = MaterialTheme.typography.body1 // 리컴포즈
                )
                HeightSpacer(dp = 4)
                DrawerCount(folderSize = { childFolderSize }, itemSize = { childItemSize })
            }
            DrawerExpandIcon(expanded = { state.expanded },
                toggleExpand = state::toggleExpand,
                dataSize = { childFolderSize })
        }

        DrawerSubCategoryDropDownMenu(selectedSubCategory = { subCategory },
            show = { state.showDropDownMenu },
            onDismiss = state::onDropdownMenuDismiss,
            subCategoryNames = filteredSubCategoryNames,
            folderNames = { childFolderNames },
            onEditSubCategoryPositiveClick = editSubCategory,
            onAddFolderPositiveClick = { parentKey, subCategoryKey, name, parent ->
                addFolder(parentKey, subCategoryKey, name, parent)
                state.expand()
            },
            offset = { longClickOffset })
    }

    val childFolders by remember {
        derivedStateOf {
            folders().filter { it.parentKey == subCategory.key }.toImmutableList()
        }
    }
    DrawerItemsWrapperWithExpandAnimation(expand = { state.expanded }) {
        drawerFolders(
            filteredFolders = { childFolders },
            filteredFolderNames = { childFolderNames }
        )
    }
}