package com.leebeebeom.clothinghelper.main.detail.dialogs

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.dialogs.EditDialog
import com.leebeebeom.clothinghelper.base.dialogs.EditDialogState
import kotlinx.collections.immutable.ImmutableList

@Composable
fun EditFolderNameDialog(
    state: EditDialogState,
    folderNames: () -> ImmutableList<String>,
    showDialog: () -> Boolean,
    onDismiss: () -> Unit,
    onPositiveButtonClick: () -> Unit
) {
    EditDialog(
        label = R.string.edit_folder_name,
        placeHolder = R.string.folder_place_holder,
        title = R.string.edit_folder_name,
        state = state,
        names = folderNames,
        existNameError = R.string.error_exist_folder_name,
        showDialog = showDialog,
        onDismiss = onDismiss,
        onPositiveButtonClick = onPositiveButtonClick
    )
}