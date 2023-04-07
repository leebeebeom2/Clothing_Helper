package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenu
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenuAddFolder
import com.leebeebeom.clothinghelper.ui.drawer.component.mainmenu.MainMenuType
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerContentWithDoubleCount(
    key: String,
    @StringRes text: Int,
    textStyle: TextStyle,
    onClick: () -> Unit,
    foldersSize: (parentKey: String) -> Int,
    folderNames: (parentKey: String) -> ImmutableSet<String>,
    itemsSize: (parentKey: String) -> Int,
    addDotIcon: Boolean = false,
    addFolder: AddFolder,
    state: DrawerItemDropdownMenuState = rememberDrawerItemDropdownMenuState(),
    subMenus: @Composable () -> Unit
) {
    val localFoldersSize by remember { derivedStateOf { foldersSize(key) } }

    DrawerRow(
        onClick = { onClick() },
        onLongClick = state::onLongClick,
        onSizeChange = state::onSizeChanged
    ) {
        if (addDotIcon) DrawerDotIcon()

        DrawerTextWithDoubleCount(text = text,
            style = textStyle,
            folderSize = { localFoldersSize },
            itemsSize = { itemsSize(key) })

        DrawerExpandIcon(expanded = { state.expanded },
            toggleExpand = state::toggleExpand,
            dataSize = { localFoldersSize })

        DrawerDropdownMenu(state = state, onDismiss = state::onDismissDropDownMenu) {
            DrawerDropdownMenuAddFolder(
                onDismissDropdownMenu = state::onDismissDropDownMenu,
                folderNames = { folderNames(key) },
                onPositiveButtonClick = { name ->
                    addFolder(MainMenuType.Archive.name, name)
                },
                expand = state::expand
            )
        }
    }

    DrawerItemsWrapperWithExpandAnimation(expand = { state.expanded }) { subMenus() }
}
