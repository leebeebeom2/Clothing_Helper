package com.leebeebeom.clothinghelper.main.base.dialogs

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.main.base.dialogs.EditDialog
import com.leebeebeom.clothinghelper.map.StableFolder
import kotlinx.collections.immutable.ImmutableList

@Composable
fun EditFolderDialog(
    folderNames: () -> ImmutableList<String>,
    show: () -> Boolean,
    onDismiss: () -> Unit,
    onPositiveButtonClick: (String) -> Unit,
    folder: () -> StableFolder
) {
    EditDialog(
        label = R.string.folder,
        placeHolder = R.string.folder_place_holder,
        title = R.string.edit_folder,
        names = folderNames,
        existNameError = R.string.error_exist_folder_name,
        onDismiss = onDismiss,
        onPositiveButtonClick = onPositiveButtonClick,
        show = show,
        initialName = { folder().name}
    )
}