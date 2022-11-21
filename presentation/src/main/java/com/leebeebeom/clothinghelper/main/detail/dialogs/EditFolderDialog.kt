package com.leebeebeom.clothinghelper.main.detail.dialogs

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.main.base.dialogs.EditDialog
import kotlinx.collections.immutable.ImmutableList

@Composable
fun EditFolderDialog(
    folderNames: () -> ImmutableList<String>,
    show: () -> Boolean,
    onDismiss: () -> Unit,
    onPositiveButtonClick: (String) -> Unit,
    initialName: String
) {
    EditDialog(
        label = R.string.edit_folder_name,
        placeHolder = R.string.folder_place_holder,
        title = R.string.edit_folder_name,
        names = folderNames,
        existNameError = R.string.error_exist_folder_name,
        onDismiss = onDismiss,
        onPositiveButtonClick = onPositiveButtonClick,
        show = show,
        initialName = initialName
    )
}