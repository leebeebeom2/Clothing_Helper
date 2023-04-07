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
    addFolder: AddFolder
) {
    DrawerDropdownMenu(state = state, onDismiss = state::onDismissDropDownMenu) {
        DrawerDropdownMenuAddFolder(
            onDismissDropdownMenu = state::onDismissDropDownMenu,
            folderNames = { folderNames(MainMenuType.Archive.name) },
            onPositiveButtonClick = { name ->
                addFolder(MainMenuType.Archive.name, name)
            },
            expand = state::expand
        )
    }
}