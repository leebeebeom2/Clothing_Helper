package com.leebeebeom.clothinghelper.main.base.selectmodebottomappbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.CircleCheckBox
import com.leebeebeom.clothinghelper.base.composables.SimpleWidthSpacer
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.main.base.noRippleClickable

@Composable
fun SelectModeBottomAppBarAllSelect(
    isAllSelected: () -> Boolean,
    onAllSelectCheckBoxClick: () -> Unit,
    selectedSubCategoriesSize: () -> Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.noRippleClickable(onAllSelectCheckBoxClick)
    ) {
        SimpleWidthSpacer(dp = 4)
        CircleCheckBox(
            isChecked = isAllSelected,
            onClick = onAllSelectCheckBoxClick,
            size = 22.dp
        )
        SimpleWidthSpacer(dp = 10)
        SelectText(selectedSubCategoriesSize = selectedSubCategoriesSize)
    }
}

@Composable
fun SelectText(selectedSubCategoriesSize: () -> Int) {
    SingleLineText(
        text = stringResource(
            id = R.string.count_selected, formatArgs = arrayOf(selectedSubCategoriesSize())
        ), modifier = Modifier.offset((-8).dp, 1.dp)
    )
}