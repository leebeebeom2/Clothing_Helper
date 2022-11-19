package com.leebeebeom.clothinghelper.main.detail.dialogs

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.dialogs.AddDialog
import com.leebeebeom.clothinghelper.base.dialogs.AddDialogState
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AddFolderDialog(
    state: AddDialogState,
    folderNames: () -> ImmutableList<String>,
    onPositiveButtonClick: (String) -> Unit
) {
    AddDialog(
        label = R.string.add_folder,
        placeHolder = R.string.folder_place_holder,
        title = R.string.add_folder,
        onPositiveButtonClick = onPositiveButtonClick,
        state = state,
        names = folderNames,
        existNameError = R.string.error_exist_folder_name
    )
}