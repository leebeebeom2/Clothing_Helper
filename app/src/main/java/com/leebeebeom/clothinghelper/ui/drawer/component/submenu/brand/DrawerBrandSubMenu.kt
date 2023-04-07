package com.leebeebeom.clothinghelper.ui.drawer.component.submenu.brand

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.IconWrapper
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.component.*
import com.leebeebeom.clothinghelper.ui.drawer.component.submenu.SubMenu
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerBrandSubMenu(
    subMenu: SubMenu,
    onClick: () -> Unit,
    folderNames: (parentKey: String) -> ImmutableSet<String>,
    foldersSize: (parentKey: String) -> Int,
    itemSize: (parentKey: String) -> Int,
    addFolder: AddFolder,
    state: DrawerItemDropdownMenuState = rememberDrawerItemDropdownMenuState(),
    folders: @Composable (parentKey: String) -> Unit
) {
    DrawerRow(
        onClick = onClick,
        onLongClick = state::onLongClick,
        onSizeChange = state::onSizeChanged
    ) {
        DrawerInsideRow {
            IconWrapper(modifier = Modifier.padding(end = 8.dp), drawable = R.drawable.ic_dot)
            DrawerTextWithDoubleCount(
                modifier = Modifier.padding(vertical = 4.dp),
                text = subMenu.name,
                style = MaterialTheme.typography.subtitle1,
                folderSize = { foldersSize(subMenu.type.name) },
                itemSize = { itemSize(subMenu.type.name) }
            )

            DrawerExpandIcon(
                expanded = { state.expanded },
                toggleExpand = state::toggleExpand,
                dataSize = { foldersSize(subMenu.type.name) }
            )
        }

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