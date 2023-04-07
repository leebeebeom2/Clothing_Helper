package com.leebeebeom.clothinghelper.ui.drawer.component.mainmenu

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenu
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenuAddFolder
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerArchiveDropdownMenu(
    state: DrawerItemDropdownMenuState,
    folderNames: (parentKey: String) -> ImmutableSet<String>,
    archive: MainMenu,
    addFolder: AddFolder
) {
    DrawerDropdownMenu(state = state, onDismiss = state::onDismissDropDownMenu) {
        DrawerDropdownMenuAddFolder(
            onDismissDropdownMenu = state::onDismissDropDownMenu,
            folderNames = { folderNames(archive.type.name) },
            onPositiveButtonClick = { name ->
                addFolder(archive.type.name, name)
            },
            expand = state::expand
        )
    }
}