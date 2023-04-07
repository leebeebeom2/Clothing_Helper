package com.leebeebeom.clothinghelper.ui.component.dialog

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun EditFolderDialog(
    show: () -> Boolean,
    onDismiss: () -> Unit,
    onPositiveButtonClick: (String) -> Unit,
    initialName: () -> String,
    folderNames: () -> ImmutableSet<String>
) {
    if (show())
        EditNameDialog(
            label = R.string.folder,
            placeHolder = R.string.folder_place_holder,
            title = R.string.edit_folder,
            existNameError = R.string.error_exist_folder_name,
            onDismiss = onDismiss,
            onPositiveButtonClick = onPositiveButtonClick,
            initialName = initialName,
            names = folderNames
        )
}