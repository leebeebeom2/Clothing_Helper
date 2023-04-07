package com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.ui.drawer.component.submenu.SubMenu
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerBrandDropdownMenu(
    state: DrawerDropdownMenuState,
    subMenu: SubMenu,
    onDismiss: () -> Unit,
    expand: () -> Unit,
    folderNames: () -> ImmutableSet<String>,
    addFolder: AddFolder
) {
    DrawerDropdownMenu(state = state, onDismiss = onDismiss) {
        DrawerDropdownMenuAddFolder(
            onDismissDropdownMenu = onDismiss,
            folderNames = folderNames,
            onPositiveButtonClick = { addFolder(subMenu.type.name, it) },
            expand = expand
        )
    }
}