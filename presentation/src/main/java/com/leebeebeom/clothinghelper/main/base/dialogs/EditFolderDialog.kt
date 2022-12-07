package com.leebeebeom.clothinghelper.main.base.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.map.StableFolder
import kotlinx.collections.immutable.ImmutableList

@Composable
fun EditFolderDialog(
    folders: () -> ImmutableList<StableFolder>,
    show: () -> Boolean,
    onDismiss: () -> Unit,
    onPositiveButtonClick: (StableFolder) -> Unit,
    folder: () -> StableFolder
) {
    val folderValue = remember { folder() }

    EditDialog(
        label = R.string.folder,
        placeHolder = R.string.folder_place_holder,
        title = R.string.edit_folder,
        items = folders,
        existNameError = R.string.error_exist_folder_name,
        onDismiss = onDismiss,
        onPositiveButtonClick = { onPositiveButtonClick(folderValue.copy(name = it)) },
        show = show,
        initialName = { folderValue.name }
    )
}