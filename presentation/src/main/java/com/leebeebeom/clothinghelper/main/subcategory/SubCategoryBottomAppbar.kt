package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomAppBar
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.SimpleIcon

@Composable
fun SubCategoryBottomAppBar(
    isSelectMode: Boolean,
    selectedSubCategoriesSize: Int,
    onAllSelectCheckBoxClick: () -> Unit,
    isAllSelected: Boolean
) {
    AnimatedVisibility(
        visible = isSelectMode,
        enter = expandVertically(expandFrom = Alignment.Top, animationSpec = tween(250)),
        exit = shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(250))
    ) {
        BottomAppBar {

            IconButton(onClick = onAllSelectCheckBoxClick) {
                CircleCheckBox(isChecked = isAllSelected, modifier = Modifier.size(20.dp))
            }
            Text(
                text = "${selectedSubCategoriesSize}개 선택됨",
                modifier = Modifier.offset((-8).dp, 1.dp)
            )
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                val editIconScale by animateFloatAsState(targetValue = if (selectedSubCategoriesSize == 1) 1f else 0f)
                val deleteIconScale by animateFloatAsState(targetValue = if (selectedSubCategoriesSize > 0) 1f else 0f)
                BottomAppBarIcon(
                    modifier = Modifier.scale(editIconScale),
                    onClick = { /*TODO*/ },
                    drawable = R.drawable.ic_edit,
                    text = R.string.change_name
                )
                BottomAppBarIcon(
                    modifier = Modifier.scale(deleteIconScale),
                    onClick = { /*TODO*/ },
                    drawable = R.drawable.ic_delete2,
                    text = R.string.delete
                )
            }
        }
    }
}

@Composable
fun BottomAppBarIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes drawable: Int,
    @StringRes text: Int
) {
    IconButton(modifier = modifier, onClick = onClick) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SimpleIcon(drawable = drawable)
            SimpleHeightSpacer(dp = 4)
            Text(
                text = stringResource(id = text),
                style = MaterialTheme.typography.caption
            )
        }
    }
}