package com.leebeebeom.clothinghelper.ui.drawer.content.submenu.clothes

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
import com.leebeebeom.clothinghelper.ui.drawer.content.folder.rememberSubBackgroundColor
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.DrawerSubMenuTag
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

@Composable
fun DrawerClothesCategory(
    state: DrawerDropdownMenuState,
    clothesCategory: ClothesCategory,
    onClick: (ClothesCategoryType) -> Unit,
    onFolderClick: OnFolderClick,
    folders: Folders,
    foldersSize: FoldersSize,
    folderNames: FolderNames,
    itemsSize: ItemsSize,
    addFolder: AddFolder,
    editFolder: EditFolder,
    deleteFolder: DeleteFolder,
    height: () -> Dp,
    onLongClick: (Offset) -> Unit,
    onSizeChanged: (IntSize) -> Unit,
    toggleExpand: () -> Unit,
    onDismissDropdownMenu: () -> Unit,
    expand: () -> Unit,
    currentBackStack: CurrentBackStack
) {
    val currentPositionBackgroundColor by rememberDrawerCurrentPositionBackgroundColorWithArg(
        currentBackStack = currentBackStack, parentKey = clothesCategory.type.name
    )

    val startPadding = remember { 16.dp }

    OpenWhenNavigateToChild(
        currentBackStack = currentBackStack,
        key = clothesCategory.type.name,
        expand = expand,
        folders = folders
    )

    DrawerContentWithDoubleCount(
        modifier = Modifier
            .padding(start = startPadding)
            .testTag(DrawerSubMenuTag),
        currentPositionBackgroundColor = { currentPositionBackgroundColor },
        state = state,
        subFoldersParentKey = clothesCategory.type.name,
        menuType = clothesCategory.type.toMenuType(),
        text = clothesCategory.name,
        textStyle = MaterialTheme.typography.subtitle1.copy(fontSize = 17.sp),
        onClick = { onClick(clothesCategory.type) },
        foldersSize = foldersSize,
        folderNames = folderNames,
        itemsSize = itemsSize,
        addFolder = addFolder,
        addDotIcon = true,
        folders = folders,
        height = height,
        onLongClick = onLongClick,
        onSizeChanged = onSizeChanged,
        toggleExpand = toggleExpand,
        onDismissDropdownMenu = onDismissDropdownMenu,
        expand = expand,
        folderBasePadding = startPadding,
        editFolder = editFolder,
        deleteFolder = deleteFolder,
        folderBaseBackground = rememberSubBackgroundColor(Black18),
        onFolderClick = onFolderClick,
        currentBackStack = currentBackStack
    )
}