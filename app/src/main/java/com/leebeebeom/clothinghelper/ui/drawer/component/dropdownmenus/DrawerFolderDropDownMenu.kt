package com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus

import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpOffset
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.ui.component.dialog.AddFolderDialog
import com.leebeebeom.clothinghelper.ui.component.dialog.EditFolderDialog
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import com.leebeebeom.clothinghelper.ui.util.EditFolder
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerFolderDropDownMenu(
    show: () -> Boolean, onDismiss: () -> Unit, offset: () -> DpOffset,
    folderNames: () -> ImmutableSet<String>,
    onAddFolderPositiveClick: AddFolder,
    onEditFolderPositiveClick: EditFolder,
    selectedFolder: () -> Folder
) {
    var showAddFolderDialog by rememberSaveable { mutableStateOf(false) }
    var showEditFolderDialog by rememberSaveable { mutableStateOf(false) }

    DropdownMenu(
        expanded = show(), onDismissRequest = onDismiss,
        offset = offset()
    ) {
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
        folderNames = folderNames,
        onPositiveButtonClick = {
            val localSelectedFolder = selectedFolder()

            onAddFolderPositiveClick(
                localSelectedFolder.key,
                localSelectedFolder.subCategoryKey,
                it,
                localSelectedFolder.mainCategoryType,
            )
        },
        show = { showAddFolderDialog },
        onDismiss = { showAddFolderDialog = false }
    )

    EditFolderDialog(
        folderNames = folderNames,
        show = { showEditFolderDialog },
        onDismiss = { showEditFolderDialog = false },
        onPositiveButtonClick = { onEditFolderPositiveClick(selectedFolder(), it) },
        initialName = { selectedFolder().name }
    )
}