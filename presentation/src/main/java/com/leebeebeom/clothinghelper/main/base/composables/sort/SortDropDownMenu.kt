package com.leebeebeom.clothinghelper.main.base.composables.sort

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.composables.SimpleWidthSpacer
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.main.base.composables.DropDownMenuRoot
import com.leebeebeom.clothinghelper.util.noRippleClickable
import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.SortPreferences

@Composable
fun SortDropdownMenu(
    show: () -> Boolean,
    sort: () -> SortPreferences,
    onSortClick: (Sort) -> Unit,
    onOrderClick: (Order) -> Unit,
    onDismiss: () -> Unit
) {
    DropDownMenuRoot(show = show, onDismiss = onDismiss) {
        Header()
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(horizontal = 12.dp)
                .padding(top = 8.dp, bottom = 4.dp)
        ) {
            SortButtons(sort, onSortClick)
            SimpleWidthSpacer(dp = 8)
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
            )
            SimpleWidthSpacer(dp = 8)
            OrderButtons(sort, onOrderClick)
        }
    }
}

@Composable
private fun Header() {
    Column(modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 4.dp)) {
        SingleLineText(
            text = R.string.sort,
            style = sortDropDownMenuHeaderStyle()
        )
        SimpleHeightSpacer(dp = 8)
        Divider()
    }
}

@Composable
private fun sortDropDownMenuHeaderStyle() =
    MaterialTheme.typography.subtitle1.copy(letterSpacing = 1.55.sp)
        .copy(color = LocalContentColor.current.copy(0.8f))

@Composable
private fun OrderButtons(
    sort: () -> SortPreferences, onOrderClick: (Order) -> Unit
) {
    val order = sort().order

    Column {
        SortButton(
            text = R.string.sort_Ascending,
            isSelected = { order == Order.ASCENDING }) { onOrderClick(Order.ASCENDING) }
        SimpleHeightSpacer(dp = 4)
        SortButton(
            text = R.string.sort_Descending,
            isSelected = { order == Order.DESCENDING }) { onOrderClick(Order.DESCENDING) }
    }
}

@Composable
private fun SortButtons(
    sort: () -> SortPreferences, onSortClick: (Sort) -> Unit
) {
    val sortValue = sort().sort

    Column {
        SortButton(
            text = R.string.sort_name,
            isSelected = { sortValue == Sort.NAME }) { onSortClick(Sort.NAME) }
        SimpleHeightSpacer(dp = 4)
        SortButton(
            text = R.string.sort_create_date,
            isSelected = { sortValue == Sort.CREATE }) { onSortClick(Sort.CREATE) }
        SimpleHeightSpacer(dp = 4)
        SortButton(
            text = R.string.sort_edit_date,
            isSelected = { sortValue == Sort.EDIT }) { onSortClick(Sort.EDIT) }
    }
}

@Composable
fun SortButton(
    @StringRes text: Int, isSelected: () -> Boolean, onSortButtonClick: () -> Unit
) {
    val strokeColor by animateColorAsState(
        targetValue = if (isSelected()) MaterialTheme.colors.primary.copy(0.8f) else Color.Transparent,
        animationSpec = tween(durationMillis = 200)
    )

    Box(
        modifier = Modifier
            .border(
                BorderStroke(width = 1.5.dp, color = strokeColor),
                shape = MaterialTheme.shapes.medium
            )
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .noRippleClickable(onClick = onSortButtonClick)
    ) {
        SingleLineText(
            text = text,
            style = MaterialTheme.typography.body2.copy(fontSize = 16.sp),
        )
    }
}