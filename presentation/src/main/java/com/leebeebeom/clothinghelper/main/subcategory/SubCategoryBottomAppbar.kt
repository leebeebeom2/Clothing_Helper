package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import com.leebeebeom.clothinghelper.base.CircleCheckBox
import com.leebeebeom.clothinghelper.base.CustomIconButton
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.SimpleWidthSpacer

@Composable
fun SubCategoryBottomAppBar(
    isSelectMode: Boolean,
    selectedSubCategoriesSize: Int,
    subCategoriesSize: Int,
    onAllSelectCheckBoxClick: () -> Unit,
    onEditSubCategoryNameClick: () -> Unit
) {
    val duration = 250

    AnimatedVisibility(
        visible = isSelectMode, enter = expandVertically(
            expandFrom = Alignment.Top, animationSpec = tween(durationMillis = duration)
        ), exit = shrinkVertically(
            shrinkTowards = Alignment.Top, animationSpec = tween(durationMillis = duration)
        )
    ) {
        BottomAppBar {
            SimpleWidthSpacer(dp = 4)
            CircleCheckBox(
                isChecked = selectedSubCategoriesSize == subCategoriesSize,
                modifier = Modifier.size(22.dp),
                onClick = onAllSelectCheckBoxClick
            )
            SimpleWidthSpacer(dp = 10)
            Text(
                text = stringResource(
                    id = R.string.count_selected, formatArgs = arrayOf(selectedSubCategoriesSize)
                ), modifier = Modifier.offset((-8).dp, 1.dp)
            )
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {

                if (selectedSubCategoriesSize == 1)
                    BottomAppBarIcon(
                        onClick = onEditSubCategoryNameClick,
                        drawable = R.drawable.ic_edit,
                        text = R.string.change_name
                    )

                if (selectedSubCategoriesSize > 0)
                    BottomAppBarIcon(
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomIconButton(modifier = modifier, onClick = onClick, drawable = drawable)
        SimpleHeightSpacer(dp = 4)
        Text(text = stringResource(id = text), style = MaterialTheme.typography.caption)
    }
}