package com.leebeebeom.clothinghelper.ui.drawer.content.submenu.clothes

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerContentWithDoubleCount
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerClothesSubMenu2(
    state: DrawerItemDropdownMenuState,
    clothesCategory: ClothesCategory,
    onClick: (ClothesCategoryType) -> Unit,
    foldersSize: (parentKey: String) -> Int,
    folderNames: (parentKey: String) -> ImmutableSet<String>,
    itemsSize: (parentKey: String) -> Int,
    addFolder: AddFolder,
    folders: @Composable (parentKey: String, basePadding: Dp) -> Unit
) {
    val startPadding = remember { 16.dp }
    DrawerContentWithDoubleCount(
        modifier = Modifier.padding(start = startPadding),
        state = state,
        key = clothesCategory.type.name,
        text = clothesCategory.name,
        textStyle = MaterialTheme.typography.subtitle1.copy(fontSize = 17.sp),
        onClick = { onClick(clothesCategory.type) },
        foldersSize = foldersSize,
        folderNames = folderNames,
        itemsSize = itemsSize,
        addFolder = addFolder,
        addDotIcon = true,
        folders = { folders(clothesCategory.type.name, startPadding) }
    )
}