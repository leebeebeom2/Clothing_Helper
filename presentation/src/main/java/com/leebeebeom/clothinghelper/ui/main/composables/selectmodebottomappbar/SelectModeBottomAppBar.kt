package com.leebeebeom.clothinghelper.ui.main.composables.selectmodebottomappbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material.BottomAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.composable.SimpleWidthSpacer
import com.leebeebeom.clothinghelper.util.Anime.SelectModeBottomAppbar.expandIn
import com.leebeebeom.clothinghelper.util.Anime.SelectModeBottomAppbar.shrinkOut

@Composable
fun BoxScope.SelectModeBottomAppBar(
    itemsSize: () -> Int,
    selectedItemsSize: () -> Int,
    onAllSelectCheckBoxClick: () -> Unit,
    show: () -> Boolean,
    editDialog: @Composable (show: Boolean, onDismiss: () -> Unit) -> Unit,
    deleteDialog: @Composable (show: Boolean, onDismiss: () -> Unit) -> Unit = { _, _ -> } // TODO
) {
    val isAllSelected by remember { derivedStateOf { selectedItemsSize() == itemsSize() } }
    val showEditIcon by remember { derivedStateOf { selectedItemsSize() == 1 } }
    val showDeleteIcon by remember { derivedStateOf { selectedItemsSize() > 0 } }

    var showEditDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    AnimatedVisibility(
        modifier = Modifier.align(Alignment.BottomCenter),
        visible = show(),
        enter = expandIn,
        exit = shrinkOut
    ) {
        BottomAppBar {
            SelectModeBottomAppBarAllSelect(
                isAllSelected = { isAllSelected },
                onClick = onAllSelectCheckBoxClick,
                selectedSize = selectedItemsSize
            )

            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                SelectModeBottomAppBarIcon(
                    visible = { showEditIcon },
                    onClick = { showEditDialog = true },
                    drawable = R.drawable.ic_edit,
                    text = R.string.edit
                )
                SimpleWidthSpacer(dp = 8)
                SelectModeBottomAppBarIcon(
                    visible = { showDeleteIcon },
                    onClick = { showDeleteDialog = true },
                    drawable = R.drawable.ic_delete2,
                    text = R.string.delete
                )
            }
            SimpleWidthSpacer(dp = 8)
        }

        editDialog(show = showEditDialog, onDismiss = { showEditDialog = false })
        deleteDialog(show = showDeleteDialog, onDismiss = { showDeleteDialog = false })
    }
}