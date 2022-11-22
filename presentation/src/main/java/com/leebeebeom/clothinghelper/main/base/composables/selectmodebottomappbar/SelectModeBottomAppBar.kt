package com.leebeebeom.clothinghelper.main.base.composables.selectmodebottomappbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.Anime.SelectModeBottomAppbar.expandIn
import com.leebeebeom.clothinghelper.base.Anime.SelectModeBottomAppbar.shrinkOut
import com.leebeebeom.clothinghelper.base.composables.SimpleWidthSpacer

@Composable
fun BoxScope.SelectModeBottomAppBar(
    selectedSize: () -> Int,
    isAllSelected: () -> Boolean,
    onAllSelectCheckBoxClick: () -> Unit,
    onEditIconClick: () -> Unit,
    onDeleteIconClick: () -> Unit = {}, // TODO
    isSelectMode: () -> Boolean,
    showEditIcon: () -> Boolean,
    showDeleteIcon: () -> Boolean
) {
    AnimatedVisibility(
        modifier = Modifier.align(Alignment.BottomCenter),
        visible = isSelectMode(),
        enter = expandIn,
        exit = shrinkOut
    ) {
        BottomAppBar {
            SelectModeBottomAppBarAllSelect(
                isAllSelected = isAllSelected,
                onClick = onAllSelectCheckBoxClick,
                selectedSize = selectedSize
            )

            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                SelectModeBottomAppBarIcon(
                    visible = showEditIcon,
                    onClick = onEditIconClick,
                    drawable = R.drawable.ic_edit,
                    text = R.string.edit
                )
                SimpleWidthSpacer(dp = 8)
                SelectModeBottomAppBarIcon(
                    visible = showDeleteIcon,
                    onClick = onDeleteIconClick,
                    drawable = R.drawable.ic_delete2,
                    text = R.string.delete
                )
            }
            SimpleWidthSpacer(dp = 8)
        }
    }
}