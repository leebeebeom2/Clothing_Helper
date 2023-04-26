package com.leebeebeom.clothinghelper.ui.drawer.content.submenu

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.ui.drawer.OpenWhenNavigateToChild
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerContentWithDoubleCount
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerCurrentPositionBackgroundColorWithArg
import com.leebeebeom.clothinghelper.ui.theme.Black18
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import com.leebeebeom.clothinghelper.ui.util.CurrentBackStack
import com.leebeebeom.clothinghelper.ui.util.DeleteFolder
import com.leebeebeom.clothinghelper.ui.util.EditFolder
import com.leebeebeom.clothinghelper.ui.util.FolderNames
import com.leebeebeom.clothinghelper.ui.util.Folders
import com.leebeebeom.clothinghelper.ui.util.FoldersSize
import com.leebeebeom.clothinghelper.ui.util.ItemsSize
import com.leebeebeom.clothinghelper.ui.util.OnFolderClick

const val DrawerSubMenuTag = "drawer sub menu"

@Composable
fun DrawerSubMenu(
    state: DrawerDropdownMenuState,
    subMenu: SubMenu,
    onClick: (SubMenuType) -> Unit,
    onFolderClick: OnFolderClick,
    folders: Folders,
    folderNames: FolderNames,
    foldersSize: FoldersSize,
    itemsSize: ItemsSize,
    addFolder: AddFolder,
    editFolder: EditFolder,
    deleteFolder: DeleteFolder,
    height: () -> Dp,
    currentBackStack: CurrentBackStack,
    onLongClick: (Offset) -> Unit,
    onSizeChanged: (IntSize) -> Unit,
    toggleExpand: () -> Unit,
    onDismissDropdownMenu: () -> Unit,
    expand: () -> Unit
) {
    val backgroundColor by rememberDrawerCurrentPositionBackgroundColorWithArg(
        currentBackStack = currentBackStack,
        parentKey = subMenu.type.name
    )

    val startPadding = remember { 8.dp }

    OpenWhenNavigateToChild(
        currentBackStack = currentBackStack,
        key = subMenu.type.name,
        folders = folders,
        expand = expand
    )

    DrawerContentWithDoubleCount(
        modifier = Modifier
            .padding(start = startPadding)
            .testTag(DrawerSubMenuTag),
        currentPositionBackgroundColor = { backgroundColor },
        state = state,
        subFoldersParentKey = subMenu.type.name,
        menuType = subMenu.type.toMenuType(),
        text = subMenu.name,
        textStyle = MaterialTheme.typography.subtitle1.copy(fontSize = 17.sp),
        onClick = { onClick(subMenu.type) },
        foldersSize = foldersSize,
        folderNames = folderNames,
        itemsSize = itemsSize,
        addFolder = addFolder,
        folders = folders,
        addDotIcon = true,
        height = height,
        onLongClick = onLongClick,
        onSizeChanged = onSizeChanged,
        toggleExpand = toggleExpand,
        onDismissDropdownMenu = onDismissDropdownMenu,
        expand = expand,
        folderBaseBackground = Black18,
        folderBasePadding = startPadding,
        onFolderClick = onFolderClick,
        editFolder = editFolder,
        deleteFolder = deleteFolder,
        currentBackStack = currentBackStack
    )
}