package com.leebeebeom.clothinghelper.ui.drawer.content.submenu

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerContentWithDoubleCount
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import kotlinx.collections.immutable.ImmutableSet

const val DrawerSubMenuTag = "drawer sub menu"

@Composable
fun DrawerSubMenu(
    state: DrawerItemDropdownMenuState,
    subMenu: SubMenu,
    onClick: (SubMenuType) -> Unit,
    folderNames: (parentKey: String) -> ImmutableSet<String>,
    foldersSize: (parentKey: String) -> Int,
    itemsSize: (parentKey: String) -> Int,
    addFolder: AddFolder,
    height: () -> Dp,
    folders: @Composable (parentKey: String, basePadding: Dp) -> Unit
) {
    val startPadding = remember { 8.dp }

    DrawerContentWithDoubleCount(
        modifier = Modifier
            .padding(start = startPadding)
            .testTag(DrawerSubMenuTag),
        state = state,
        key = subMenu.type.name,
        menuType = subMenu.type.toMenuType(),
        text = subMenu.name,
        textStyle = MaterialTheme.typography.subtitle1.copy(fontSize = 17.sp),
        onClick = { onClick(subMenu.type) },
        foldersSize = foldersSize,
        folderNames = folderNames,
        itemsSize = itemsSize,
        addFolder = addFolder,
        folders = { folders(subMenu.type.name, startPadding) },
        addDotIcon = true,
        height = height
    )
}