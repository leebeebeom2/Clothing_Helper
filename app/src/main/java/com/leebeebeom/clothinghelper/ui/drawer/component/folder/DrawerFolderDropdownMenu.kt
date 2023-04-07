package com.leebeebeom.clothinghelper.ui.drawer.component.folder

import androidx.compose.runtime.*
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.ui.component.dialog.AddFolderDialog
import com.leebeebeom.clothinghelper.ui.component.dialog.EditFolderDialog
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenu
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenuItem
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenuState
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import com.leebeebeom.clothinghelper.ui.util.EditFolder
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerDownMenuEditAndAddFolder(
    state: DrawerDropdownMenuState,
    onDismiss: () -> Unit,
    expand: () -> Unit,
    folderNames: () -> ImmutableSet<String>,
    childFolderNames: () -> ImmutableSet<String>,
    addFolder: AddFolder,
    editFolder: EditFolder,
    selectedFolder: () -> Folder
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    val localSelectedFolder by remember(selectedFolder) { derivedStateOf(selectedFolder) }

    DrawerDropdownMenu(state = state, onDismiss = onDismiss) {
        DrawerDropdownMenuItem(text = R.string.dropdown_menu_edit) {
            showEditDialog = true
            onDismiss()
        }

        DrawerDropdownMenuItem(text = R.string.dropdown_menu_add_folder) {
            showAddDialog = true
            onDismiss()
        }
    }

    EditFolderDialog(
        folderNames = folderNames,
        onPositiveButtonClick = { name -> editFolder(localSelectedFolder, name) },
        show = { showEditDialog },
        onDismiss = { showEditDialog = false },
        initialName = { localSelectedFolder.name }
    )

    AddFolderDialog(
        folderNames = childFolderNames,
        onPositiveButtonClick = { name ->
            addFolder(localSelectedFolder.key, name)
            expand()
        }, show = { showAddDialog }, onDismiss = { showAddDialog = false })
}