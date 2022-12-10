package com.leebeebeom.clothinghelper.ui.main.root.contents.dropdownmenus

import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.*
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.ui.main.dialogs.AddFolderDialog
import com.leebeebeom.clothinghelper.ui.main.dialogs.EditFolderDialog
import com.leebeebeom.clothinghelper.util.AddFolder
import com.leebeebeom.clothinghelper.util.EditFolder
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerFolderDropDownMenu(
    show: () -> Boolean, onDismiss: () -> Unit,
    folders: () -> ImmutableList<StableFolder>,
    onAddFolderPositiveClick: AddFolder,
    onEditFolderPositiveClick: EditFolder,
    selectedFolder: () -> StableFolder
) {
    var showAddFolderDialog by remember { mutableStateOf(false) }
    var showEditFolderDialog by remember { mutableStateOf(false) }

    DropdownMenu(expanded = show(), onDismissRequest = onDismiss) {
        DrawerDropdownMenuItem(
            text = R.string.add_folder,
            onClick = { showAddFolderDialog = true },
            onDismiss = onDismiss
        )

        DrawerDropdownMenuItem(
            text = R.string.edit_folder,
            onClick = { showEditFolderDialog = true },
            onDismiss = onDismiss
        )
    }

    AddFolderDialog(
        folders = folders,
        onPositiveButtonClick = {
            val parent = selectedFolder()
            onAddFolderPositiveClick(
                parent.key,
                parent.subCategoryKey,
                it,
                parent.parent,
            )
        },
        show = { showAddFolderDialog },
        onDismiss = { showAddFolderDialog = false }
    )

    EditFolderDialog(
        folders = folders,
        show = { showEditFolderDialog },
        onDismiss = { showEditFolderDialog = false },
        onPositiveButtonClick = onEditFolderPositiveClick,
        selectedFolder = selectedFolder
    )
}