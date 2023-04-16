package com.leebeebeom.clothinghelper.ui.drawer.content.folder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.ui.component.dialog.AddFolderDialog
import com.leebeebeom.clothinghelper.ui.component.dialog.DeleteFolderDialog
import com.leebeebeom.clothinghelper.ui.component.dialog.EditFolderDialog
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenu
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenuItem
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenuState
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import com.leebeebeom.clothinghelper.ui.util.EditFolder
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerDownMenuEditAndAddFolder( // skippable
    state: DrawerDropdownMenuState,
    onDismiss: () -> Unit,
    expand: () -> Unit,
    folderNames: () -> ImmutableSet<String>,
    childFolderNames: () -> ImmutableSet<String>,
    addFolder: AddFolder,
    editFolder: EditFolder,
    deleteFolder: (Folder) -> Unit,
    selectedFolder: () -> Folder
) {
    var showEditDialog by rememberSaveable { mutableStateOf(false) }
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

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

        DrawerDropdownMenuItem(text = R.string.dropdown_menu_delete) {
            showDeleteDialog = true
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
            addFolder(localSelectedFolder.key, name, localSelectedFolder.menuType)
            expand()
        }, show = { showAddDialog }, onDismiss = { showAddDialog = false })

    DeleteFolderDialog(
        onDismiss = { showDeleteDialog = false },
        toBeDeletedFolders = listOf(selectedFolder()),
        show = { showDeleteDialog },
        onPositiveButtonClick = {
            deleteFolder(selectedFolder())
        }
    )
}