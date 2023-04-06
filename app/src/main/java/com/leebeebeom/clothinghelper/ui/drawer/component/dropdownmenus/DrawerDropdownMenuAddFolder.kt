package com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus

import androidx.compose.runtime.*
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.dialog.AddFolderDialog
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerDropdownMenuAddFolder(
    onDismiss: () -> Unit,
    folderNames: () -> ImmutableSet<String>,
    onPositiveButtonClick: (name: String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    DrawerDropdownMenuItem(text = R.string.dropdown_menu_add_folder) { showDialog = true }

    AddFolderDialog(folderNames = folderNames, onPositiveButtonClick = {
        onPositiveButtonClick(it)
        onDismiss()
    }, show = { showDialog }, onDismiss = { showDialog = false })
}