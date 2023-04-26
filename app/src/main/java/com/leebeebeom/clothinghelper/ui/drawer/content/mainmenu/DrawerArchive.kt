package com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.MenuType
import com.leebeebeom.clothinghelper.ui.drawer.OpenWhenNavigateToChild
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerContentWithDoubleCount
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerCurrentPositionBackgroundColorWithArg
import com.leebeebeom.clothinghelper.ui.theme.Black11
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import com.leebeebeom.clothinghelper.ui.util.CurrentBackStack
import com.leebeebeom.clothinghelper.ui.util.DeleteFolder
import com.leebeebeom.clothinghelper.ui.util.EditFolder
import com.leebeebeom.clothinghelper.ui.util.FolderNames
import com.leebeebeom.clothinghelper.ui.util.Folders
import com.leebeebeom.clothinghelper.ui.util.FoldersSize
import com.leebeebeom.clothinghelper.ui.util.ItemsSize
import com.leebeebeom.clothinghelper.ui.util.OnFolderClick

@Composable
fun DrawerArchive(
    state: DrawerDropdownMenuState,
    onClick: (MainMenuType) -> Unit,
    onFolderClick: OnFolderClick,
    folders: Folders,
    folderNames: FolderNames,
    foldersSize: FoldersSize,
    itemsSize: ItemsSize,
    addFolder: AddFolder,
    editFolder: EditFolder,
    deleteFolder: DeleteFolder,
    onSizeChanged: (height: Int) -> Unit,
    currentBackStack: CurrentBackStack,
    onLongClick: (Offset) -> Unit,
    onChildSizeChange: (IntSize) -> Unit,
    toggleExpand: () -> Unit,
    onDismissDropdownMenu: () -> Unit,
    expand: () -> Unit
) {
    val currentPositionBackgroundColor by rememberDrawerCurrentPositionBackgroundColorWithArg(
        currentBackStack = currentBackStack,
        parentKey = MainMenuType.Archive.name
    )

    OpenWhenNavigateToChild(
        currentBackStack = currentBackStack,
        folders = folders,
        expand = expand,
        key = MainMenuType.Archive.name
    )

    DrawerContentWithDoubleCount(
        modifier = Modifier
            .testTag(DrawerMainMenuTag)
            .onSizeChanged { onSizeChanged(it.height) },
        currentPositionBackgroundColor = { currentPositionBackgroundColor },
        subFoldersParentKey = MainMenuType.Archive.name,
        menuType = MenuType.Archive,
        text = R.string.archive_cap,
        textStyle = drawerMainMenuTextStyle(),
        onClick = { onClick(MainMenuType.Archive) },
        folders = folders,
        foldersSize = foldersSize,
        folderNames = folderNames,
        itemsSize = itemsSize,
        addFolder = addFolder,
        editFolder = editFolder,
        deleteFolder = deleteFolder,
        state = state,
        height = { 0.dp },
        onLongClick = onLongClick,
        onSizeChanged = onChildSizeChange,
        toggleExpand = toggleExpand,
        onDismissDropdownMenu = onDismissDropdownMenu,
        expand = expand,
        folderBaseBackground = Black11,
        onFolderClick = onFolderClick,
        currentBackStack = currentBackStack,
        folderBasePadding = 0.dp,
    )
}