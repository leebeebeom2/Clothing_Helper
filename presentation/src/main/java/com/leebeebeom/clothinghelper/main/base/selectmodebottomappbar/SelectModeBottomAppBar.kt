package com.leebeebeom.clothinghelper.main.base.selectmodebottomappbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.Anime.BottomAppbar.expandIn
import com.leebeebeom.clothinghelper.base.Anime.BottomAppbar.shrinkOut
import com.leebeebeom.clothinghelper.base.composables.SimpleWidthSpacer

@Composable
fun BoxScope.SelectModeBottomAppBar(
    selectedSubCategoriesSize: () -> Int,
    isAllSelected: () -> Boolean,
    showEditIcon: () -> Boolean,
    showDeleteIcon: () -> Boolean,
    onAllSelectCheckBoxClick: () -> Unit,
    onEditSubCategoryNameClick: () -> Unit,
    isSelectMode: () -> Boolean
) {
    AnimatedVisibility(
        modifier = Modifier.align(Alignment.BottomCenter),
        visible = isSelectMode(),
        enter = expandIn,
        exit = shrinkOut
    ) {
        BottomAppBar {
            SelectModeBottomAppBarAllSelect(
                isAllSelected,
                onAllSelectCheckBoxClick,
                selectedSubCategoriesSize
            )

            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                SelectModeBottomAppBarIcon(
                    visible = showEditIcon,
                    onClick = onEditSubCategoryNameClick,
                    drawable = R.drawable.ic_edit,
                    text = R.string.change_name
                )
                SelectModeBottomAppBarIcon(
                    visible = showDeleteIcon,
                    onClick = { /*TODO*/ },
                    drawable = R.drawable.ic_delete2,
                    text = R.string.delete
                )
            }
            SimpleWidthSpacer(dp = 4)
        }
    }
}