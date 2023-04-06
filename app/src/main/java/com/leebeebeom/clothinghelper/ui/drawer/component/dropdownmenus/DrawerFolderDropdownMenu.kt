package com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import com.leebeebeom.clothinghelper.ui.util.EditFolder
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerFolderDropDownMenu(
    state: DrawerDropdownMenuState,
    onDismiss: () -> Unit,
    folderNames: () -> ImmutableSet<String>,
    childFolderNames: () -> ImmutableSet<String>,
    addFolder: AddFolder,
    editFolder: EditFolder,
    selectedFolder: () -> Folder
) {
    DrawerDropdownMenu(state = state, onDismiss = onDismiss) {
        DrawerDropdownMenuEditFolder(
            onDismiss = onDismiss,
            folderNames = folderNames,
            onPositiveButtonClick = { name -> editFolder(selectedFolder(), name) },
            initialName = { selectedFolder().name }
        )

        DrawerDropdownMenuAddFolder(
            onDismiss = onDismiss,
            folderNames = childFolderNames,
            onPositiveButtonClick = { name -> addFolder(selectedFolder().key, name) }
        )
    }
}