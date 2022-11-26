package com.leebeebeom.clothinghelper.main.detail

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.SimpleIcon
import com.leebeebeom.clothinghelper.main.base.composables.Fab
import com.leebeebeom.clothinghelper.main.base.dialogs.AddFolderDialog
import com.leebeebeom.clothinghelper.map.StableFolder
import kotlinx.collections.immutable.ImmutableList

@Composable
fun BoxScope.DetailFab(
    isRevealed: () -> Boolean,
    onAddFolderPositiveButtonClick: (String) -> Unit,
    folders: () -> ImmutableList<StableFolder>,
    isSelectMode: () -> Boolean
) {
    var showAddDialog by rememberSaveable { mutableStateOf(false) }

    Fab(
        visible = { !isSelectMode() },
        onClick = {
            if (isRevealed()) {
                showAddDialog = true
            } else {
                // TODO
            }
        }
    ) {
        SimpleIcon(drawable = R.drawable.ic_add)
    }

    AddFolderDialog(
        folders = folders,
        onPositiveButtonClick = onAddFolderPositiveButtonClick,
        show = { showAddDialog },
        onDismiss = { showAddDialog = false }
    )
}