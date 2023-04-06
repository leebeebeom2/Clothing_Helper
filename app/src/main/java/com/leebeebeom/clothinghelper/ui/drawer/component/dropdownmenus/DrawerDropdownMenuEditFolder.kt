package com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus

import androidx.compose.runtime.*
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.dialog.EditFolderDialog
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerDropdownMenuEditFolder(
    onDismiss: () -> Unit,
    folderNames: () -> ImmutableSet<String>,
    onPositiveButtonClick: (name: String) -> Unit,
    initialName: () -> String
) {
    var showDialog by remember { mutableStateOf(false) }

    DrawerDropdownMenuItem(text = R.string.dropdown_menu_edit) { showDialog = true }

    EditFolderDialog(
        folderNames = folderNames,
        onPositiveButtonClick = {
            onPositiveButtonClick(it)
            onDismiss()
        },
        show = { showDialog },
        onDismiss = { showDialog = false },
        initialName = initialName
    )
}