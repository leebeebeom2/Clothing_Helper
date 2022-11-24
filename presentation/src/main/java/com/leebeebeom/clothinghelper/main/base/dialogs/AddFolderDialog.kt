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
fun AddFolderDialog(
    folders: () -> ImmutableList<StableFolder>,
    onPositiveButtonClick: (String) -> Unit,
    show: () -> Boolean,
    onDismiss: () -> Unit
) {
    val names by remember { derivedStateOf { folders().map { it.name }.toImmutableList() } }

    AddDialog(
        label = R.string.folder,
        placeHolder = R.string.folder_place_holder,
        title = R.string.add_folder,
        onPositiveButtonClick = onPositiveButtonClick,
        names = { names },
        existNameError = R.string.error_exist_folder_name,
        show = show,
        onDismiss = onDismiss
    )
}