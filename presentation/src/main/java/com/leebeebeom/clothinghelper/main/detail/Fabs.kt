package com.leebeebeom.clothinghelper.main.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.SimpleIcon
import com.leebeebeom.clothinghelper.base.dialogs.AddDialogState
import com.leebeebeom.clothinghelper.main.base.components.Fab
import com.leebeebeom.clothinghelper.main.base.components.fabBottomPadding
import com.leebeebeom.clothinghelper.main.base.components.fabEndPadding
import com.leebeebeom.clothinghelper.main.detail.dialogs.AddFolderDialog
import com.leebeebeom.clothinghelper.map.StableFolder
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DetailFab(
    isFabSpread: () -> Boolean,
    onClick: () -> Unit,
    onAddFolderPositiveButtonClick: (String) -> Unit,
    folders: () -> ImmutableList<StableFolder>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = fabEndPadding.dp, bottom = fabBottomPadding.dp)
    ) {
        Box(modifier = Modifier.align(Alignment.BottomEnd)) {
            AddFolderFab(
                isFabSpread = isFabSpread,
                onClick = onClick,
                onAddFolderPositiveButtonClick = onAddFolderPositiveButtonClick,
                folders = folders
            )
            AddContentFab(isFabSpread = isFabSpread, onClick = onClick)
            Fab(
                visible = { true },
                onClick = onClick,
                paddingValues = PaddingValues(),
                align = Alignment.Center
            ) {
                val rotate by animateFloatAsState(targetValue = if (isFabSpread()) 136f else 0f)
                SimpleIcon(drawable = R.drawable.ic_add, modifier = Modifier.rotate(rotate))
            }
        }
    }
}

@Composable
private fun BoxScope.AddFolderFab(
    isFabSpread: () -> Boolean,
    onClick: () -> Unit,
    onAddFolderPositiveButtonClick: (String) -> Unit,
    folders: () -> ImmutableList<StableFolder>
) {
    val offsetY by animateIntAsState(targetValue = if (isFabSpread()) -260 else 0)
    val dialogState = rememberSaveable(saver = AddDialogState.Saver) { AddDialogState() }

    Fab(
        size = 36.dp,
        paddingValues = PaddingValues(),
        align = Alignment.Center,
        visible = { true },
        onClick = {
            dialogState.showDialog()
            onClick()
        },
        modifier = Modifier.offset { IntOffset(x = 0, y = offsetY) },
    ) { SimpleIcon(drawable = R.drawable.ic_create_new_folder) }

    AddFolderDialog(
        state = dialogState,
        folders = folders,
        onPositiveButtonClick = onAddFolderPositiveButtonClick
    )
}

@Composable
private fun BoxScope.AddContentFab(isFabSpread: () -> Boolean, onClick: () -> Unit) {
    val offsetY by animateIntAsState(targetValue = if (isFabSpread()) -136 else 0)
    Fab(
        size = 36.dp,
        paddingValues = PaddingValues(),
        align = Alignment.Center,
        visible = { true },
        onClick = onClick,
        modifier = Modifier.offset { IntOffset(x = 0, y = offsetY) },
    ) { SimpleIcon(drawable = R.drawable.ic_add_content) }
}