package com.leebeebeom.clothinghelper.main.base.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.map.StableFolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun EditFolderDialog(
    folders: () -> ImmutableList<StableFolder>,
    show: () -> Boolean,
    onDismiss: () -> Unit,
    onPositiveButtonClick: (String) -> Unit,
    folder: () -> StableFolder
) {
    val names by remember { derivedStateOf { folders().map { it.name }.toImmutableList() } }

    EditDialog(
        label = R.string.folder,
        placeHolder = R.string.folder_place_holder,
        title = R.string.edit_folder,
        names = { names },
        existNameError = R.string.error_exist_folder_name,
        onDismiss = onDismiss,
        onPositiveButtonClick = onPositiveButtonClick,
        show = show,
        initialName = { folder().name }
    )
}