package com.leebeebeom.clothinghelper.ui.main.composables.selectmodebottomappbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.composable.SimpleWidthSpacer
import com.leebeebeom.clothinghelper.composable.SingleLineText
import com.leebeebeom.clothinghelper.ui.main.composables.CircleCheckBox
import com.leebeebeom.clothinghelper.util.noRippleClickable

@Composable
fun SelectModeBottomAppBarAllSelect(
    itemsSize: () -> Int,
    onClick: () -> Unit,
    selectedItemsSize: () -> Int
) {
    val isAllSelected by remember { derivedStateOf { selectedItemsSize() == itemsSize() } }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.noRippleClickable(onClick)
    ) {
        SimpleWidthSpacer(dp = 4)
        CircleCheckBox(isChecked = { isAllSelected }, onClick = onClick, size = 22.dp)
        SimpleWidthSpacer(dp = 10)
        SelectText(selectedSize = selectedItemsSize)
    }
}

@Composable
private fun SelectText(selectedSize: () -> Int) {
    SingleLineText(
        text = stringResource(
            id = R.string.count_selected, formatArgs = arrayOf(selectedSize())
        ), modifier = Modifier.offset((-8).dp, 0.dp)
    )
}