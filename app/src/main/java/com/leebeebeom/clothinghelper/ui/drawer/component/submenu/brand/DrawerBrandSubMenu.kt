package com.leebeebeom.clothinghelper.ui.drawer.component.submenu.brand

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.component.*
import com.leebeebeom.clothinghelper.ui.drawer.component.submenu.SubMenu
import com.leebeebeom.clothinghelper.ui.drawer.component.submenu.SubMenuType
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemDropdownMenuState
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
    state: DrawerItemDropdownMenuState = rememberDrawerItemDropdownMenuState(),
    folders: @Composable (parentKey: String) -> Unit
) {
    DrawerRow(
        onClick = { onClick(subMenu.type) },
        onLongClick = state::onLongClick,
        onSizeChange = state::onSizeChanged
    ) {
        DrawerDotIcon()
        DrawerTextWithDoubleCount(
            text = subMenu.name,
            style = MaterialTheme.typography.subtitle1,
            folderSize = { foldersSize(subMenu.type.name) },
            itemsSize = { itemsSize(subMenu.type.name) }
        )

        DrawerExpandIcon(
            expanded = { state.expanded },
            toggleExpand = state::toggleExpand,
            dataSize = { foldersSize(subMenu.type.name) }
        )

        DrawerBrandDropdownMenu(
            state = state,
            subMenu = subMenu,
            onDismiss = state::onDismissDropDownMenu,
            folderNames = { folderNames(subMenu.type.name) },
            addFolder = addFolder,
            expand = state::expand
        )
    }
    DrawerItemsWrapperWithExpandAnimation(expand = { state.expanded }) { folders(subMenu.type.name) }
}