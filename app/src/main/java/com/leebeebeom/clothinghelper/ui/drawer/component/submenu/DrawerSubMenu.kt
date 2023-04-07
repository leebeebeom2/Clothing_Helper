package com.leebeebeom.clothinghelper.ui.drawer.component.submenu.submenu

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerContentWithDoubleCount
import com.leebeebeom.clothinghelper.ui.drawer.component.submenu.SubMenu
import com.leebeebeom.clothinghelper.ui.drawer.component.submenu.SubMenuType
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerBrandSubMenu(
    state: DrawerItemDropdownMenuState,
    subMenu: SubMenu,
    onClick: (SubMenuType) -> Unit,
    folderNames: (parentKey: String) -> ImmutableSet<String>,
    foldersSize: (parentKey: String) -> Int,
    itemsSize: (parentKey: String) -> Int,
    addFolder: AddFolder,
    folders: @Composable (parentKey: String) -> Unit
) {
    DrawerContentWithDoubleCount(
        modifier = Modifier.padding(start = 8.dp),
        state = state,
        key = subMenu.type.name,
        text = subMenu.name,
        textStyle = MaterialTheme.typography.subtitle1,
        onClick = { onClick(subMenu.type) },
        foldersSize = foldersSize,
        folderNames = folderNames,
        itemsSize = itemsSize,
        addFolder = addFolder,
        folders = { folders(subMenu.type.name) },
        addDotIcon = true
    )
}