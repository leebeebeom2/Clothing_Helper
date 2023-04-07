package com.leebeebeom.clothinghelper.ui.drawer.component.folder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenu
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenuAddFolder
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenuEditFolder
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenuState
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import com.leebeebeom.clothinghelper.ui.util.EditFolder
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerFolderDropDownMenu(
    state: DrawerDropdownMenuState,
    onDismiss: () -> Unit,
    expand: () -> Unit,
    folderNames: () -> ImmutableSet<String>,
    childFolderNames: () -> ImmutableSet<String>,
    addFolder: AddFolder,
    editFolder: EditFolder,
    selectedFolder: () -> Folder
) {
    val localSelectedFolder by remember(selectedFolder) { derivedStateOf(selectedFolder) }

    DrawerDropdownMenu(state = state, onDismiss = onDismiss) {
        DrawerDropdownMenuEditFolder(
            onDismiss = onDismiss,
            folderNames = folderNames,
            onPositiveButtonClick = { name -> editFolder(localSelectedFolder, name) },
            initialName = { localSelectedFolder.name }
        )

        DrawerDropdownMenuAddFolder(
            onDismissDropdownMenu = onDismiss,
            folderNames = childFolderNames,
            onPositiveButtonClick = { name -> addFolder(localSelectedFolder.key, name) },
            expand = expand
        )
    }
}