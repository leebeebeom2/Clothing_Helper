package com.leebeebeom.clothinghelper.main.subcategory.content

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomAppBar
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.Anime.BottomAppbar.expandIn
import com.leebeebeom.clothinghelper.base.Anime.BottomAppbar.shrinkOut
import com.leebeebeom.clothinghelper.base.CircleCheckBox
import com.leebeebeom.clothinghelper.base.CustomIconButton
import com.leebeebeom.clothinghelper.base.SimpleWidthSpacer

@Composable
fun BoxScope.SubCategoryBottomAppBar(
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
            SimpleWidthSpacer(dp = 4)
            CircleCheckBox(
                isChecked = isAllSelected,
                modifier = Modifier.size(22.dp),
                onClick = onAllSelectCheckBoxClick
            )
            SimpleWidthSpacer(dp = 10)
            SelectText(selectedSubCategoriesSize = selectedSubCategoriesSize)
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                BottomAppBarIcon(
                    visible = showEditIcon,
                    onClick = onEditSubCategoryNameClick,
                    drawable = R.drawable.ic_edit,
                    text = R.string.change_name
                )
                BottomAppBarIcon(
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

@Composable
private fun BottomAppBarIcon(
    visible: () -> Boolean, onClick: () -> Unit, @DrawableRes drawable: Int, @StringRes text: Int
) {
    if (visible()) Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CustomIconButton(onClick = onClick, drawable = drawable)
        Text(text = stringResource(id = text), style = MaterialTheme.typography.caption)
    }
}

@Composable
fun SelectText(selectedSubCategoriesSize: () -> Int) {
    Text(
        text = stringResource(
            id = R.string.count_selected, formatArgs = arrayOf(selectedSubCategoriesSize())
        ), modifier = Modifier.offset((-8).dp, 1.dp)
    )
}