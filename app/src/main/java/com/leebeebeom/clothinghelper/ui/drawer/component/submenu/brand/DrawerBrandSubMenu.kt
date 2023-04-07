package com.leebeebeom.clothinghelper.ui.drawer.component.submenu.brand

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerContentWithDoubleCount
import com.leebeebeom.clothinghelper.ui.drawer.component.submenu.SubMenu
import com.leebeebeom.clothinghelper.ui.drawer.component.submenu.SubMenuType
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerBrandSubMenu(
    subMenu: SubMenu,
    onClick: (SubMenuType) -> Unit,
    folderNames: (parentKey: String) -> ImmutableSet<String>,
    foldersSize: (parentKey: String) -> Int,
    itemsSize: (parentKey: String) -> Int,
    addFolder: AddFolder,
    folders: @Composable (parentKey: String) -> Unit
) {
    DrawerContentWithDoubleCount(
        key = subMenu.type.name,
        text = subMenu.name,
        textStyle = MaterialTheme.typography.subtitle1,
        onClick = { onClick(subMenu.type) },
        foldersSize = foldersSize,
        folderNames = folderNames,
        itemsSize = itemsSize,
        addFolder = addFolder,
        subMenus = { folders(subMenu.type.name) }, addDotIcon = true
    )
}