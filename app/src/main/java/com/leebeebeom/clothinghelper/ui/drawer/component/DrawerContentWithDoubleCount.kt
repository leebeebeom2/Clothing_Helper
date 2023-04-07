package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.annotation.StringRes
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.TextStyle
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.dialog.AddFolderDialog
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenu
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenuItem
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
    state: DrawerItemDropdownMenuState,
    folders: @Composable () -> Unit
) {
    val localFolderNames by remember(folderNames) { derivedStateOf { folderNames(key) } }
    val localFoldersSize by remember(foldersSize) { derivedStateOf { foldersSize(key) } }
    val localItemsSize by remember(itemsSize) { derivedStateOf { itemsSize(key) } }

    DrawerRow(
        onClick = onClick, onLongClick = state::onLongClick, onSizeChange = state::onSizeChanged
    ) {
        if (addDotIcon) DrawerDotIcon()

        DrawerTextWithDoubleCount(text = text,
            style = textStyle,
            foldersSize = { localFoldersSize },
            itemsSize = { localItemsSize })

        DrawerExpandIcon(expanded = { state.expanded },
            toggleExpand = state::toggleExpand,
            dataSize = { localFoldersSize })

        var showAddDialog by rememberSaveable { mutableStateOf(false) }

        DrawerDropdownMenu(state = state, onDismiss = state::onDismissDropDownMenu) {
            DrawerDropdownMenuItem(text = R.string.dropdown_menu_add_folder) {
                showAddDialog = true
                state.onDismissDropDownMenu()
            }
        }

        AddFolderDialog(
            folderNames = { localFolderNames },
            onPositiveButtonClick = { name ->
                addFolder(key, name)
                state.expand()
            }, show = { showAddDialog }, onDismiss = { showAddDialog = false })
    }

    DrawerItemsWrapperWithExpandAnimation(expand = { state.expanded }) { folders() }
}
