package com.leebeebeom.clothinghelper.main.root.contents.dropdownmenus

import androidx.compose.runtime.*
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.main.base.composables.DropDownMenuRoot
import com.leebeebeom.clothinghelper.main.base.dialogs.AddFolderDialog
import com.leebeebeom.clothinghelper.main.base.dialogs.EditFolderDialog
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.util.AddFolder
import com.leebeebeom.clothinghelper.util.EditFolder
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerFolderDropDownMenu(
    show: () -> Boolean, onDismiss: () -> Unit,
    folders: () -> ImmutableList<StableFolder>,
    onAddFolderPositiveClick: AddFolder,
    onEditFolderPositiveClick: EditFolder,
    folder: () -> StableFolder
) {
    var showAddFolderDialog by remember { mutableStateOf(false) }
    var showEditFolderDialog by remember { mutableStateOf(false) }

    DropDownMenuRoot(show = show, onDismiss = onDismiss) {
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
            val parent = folder()
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
        selectedFolder = folder
    )
}