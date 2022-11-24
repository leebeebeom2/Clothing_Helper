package com.leebeebeom.clothinghelper.main.base.dialogs

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.map.StableFolder
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AddFolderDialog(
    folders: () -> ImmutableList<StableFolder>,
    onPositiveButtonClick: (String) -> Unit,
    show: () -> Boolean,
    onDismiss: () -> Unit
) {
    AddDialog(
        label = R.string.folder,
        placeHolder = R.string.folder_place_holder,
        title = R.string.add_folder,
        onPositiveButtonClick = onPositiveButtonClick,
        items = folders,
        existNameError = R.string.error_exist_folder_name,
        show = show,
        onDismiss = onDismiss
    )
}