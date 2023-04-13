package com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.MenuType
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerContentWithDoubleCount
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerArchive(
    state: DrawerItemDropdownMenuState,
    onClick: (MainMenuType) -> Unit,
    folderNames: (parentKey: String) -> ImmutableSet<String>,
    foldersSize: (parentKey: String) -> Int,
    itemsSize: (parentKey: String) -> Int,
    addFolder: AddFolder,
    folders: @Composable () -> Unit,
) {
    DrawerContentWithDoubleCount(
        key = MainMenuType.Archive.name,
        menuType = MenuType.Archive,
        text = R.string.archive_cap,
        textStyle = drawerMainMenuTextStyle(),
        onClick = { onClick(MainMenuType.Archive) },
        foldersSize = foldersSize,
        folderNames = folderNames,
        itemsSize = itemsSize,
        addFolder = addFolder,
        folders = folders,
        state = state
    )
}