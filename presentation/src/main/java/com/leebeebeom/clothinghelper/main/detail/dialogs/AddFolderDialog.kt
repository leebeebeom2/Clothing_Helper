package com.leebeebeom.clothinghelper.main.detail.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.dialogs.AddDialog
import com.leebeebeom.clothinghelper.base.dialogs.AddDialogState
import com.leebeebeom.clothinghelper.map.StableFolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun AddFolderDialog(
    state: AddDialogState,
    folders: () -> ImmutableList<StableFolder>,
    onPositiveButtonClick: (String) -> Unit
) {
    val names by remember { derivedStateOf { folders().map { it.name }.toImmutableList() } }

    AddDialog(
        label = R.string.add_folder,
        placeHolder = R.string.folder_place_holder,
        title = R.string.add_folder,
        onPositiveButtonClick = onPositiveButtonClick,
        state = state,
        names = { names },
        existNameError = R.string.error_exist_folder_name
    )
}