package com.leebeebeom.clothinghelper.ui.component.dialog

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun AddFolderDialog(
    folderNames: () -> ImmutableSet<String>,
    onPositiveButtonClick: (name: String) -> Unit,
    show: () -> Boolean,
    onDismiss: () -> Unit
) {
    if (show())
        AddDialog(
            label = R.string.folder,
            placeHolder = R.string.folder_place_holder,
            title = R.string.add_folder,
            existNameError = R.string.error_exist_folder_name,
            onPositiveButtonClick = onPositiveButtonClick,
            names = folderNames,
            onDismiss = onDismiss
        )
}