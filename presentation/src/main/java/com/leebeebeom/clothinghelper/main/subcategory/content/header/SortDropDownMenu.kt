package com.leebeebeom.clothinghelper.main.subcategory.content.header

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.DropDownMenuRoot
import com.leebeebeom.clothinghelper.base.composables.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.composables.SimpleWidthSpacer
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
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
                .padding(vertical = 8.dp, horizontal = 12.dp)
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
private fun OrderButtons(
    sort: () -> SortPreferences,
    onOrderClick: (Order) -> Unit
) {
    Column {
        SortButton(
            text = R.string.sort_Ascending,
            isSelected = { sort().order == Order.ASCENDING }
        ) { onOrderClick(Order.ASCENDING) }
        SimpleHeightSpacer(dp = 8)
        SortButton(
            text = R.string.sort_Descending,
            isSelected = { sort().order == Order.DESCENDING }
        ) { onOrderClick(Order.DESCENDING) }
    }
}

@Composable
private fun SortButtons(
    sort: () -> SortPreferences,
    onSortClick: (Sort) -> Unit
) {
    Column {
        SortButton(
            text = R.string.sort_name,
            isSelected = { sort().sort == Sort.NAME }
        ) { onSortClick(Sort.NAME) }
        SimpleHeightSpacer(dp = 8)
        SortButton(
            text = R.string.sort_create_date,
            isSelected = { sort().sort == Sort.CREATE }
        ) { onSortClick(Sort.CREATE) }
        SimpleHeightSpacer(dp = 8)
        SortButton(
            text = R.string.sort_create_date,
            isSelected = { sort().sort == Sort.EDIT }
        ) { onSortClick(Sort.EDIT) }
    }
}

@Composable
private fun Header() {
    Column(modifier = Modifier.padding(start = 12.dp, top = 8.dp)) {
        SingleLineText(
            text = R.string.sort,
            style = MaterialTheme.typography.subtitle2.copy(letterSpacing = 1.55.sp)
                .copy(color = LocalContentColor.current.copy(0.8f))
        )
        SimpleHeightSpacer(dp = 8)
        Divider()
    }
}

@Composable
fun SortButton(
    @StringRes text: Int,
    isSelected: () -> Boolean,
    onSortButtonClick: () -> Unit
) {
    val strokeColor by animateColorAsState(
        targetValue = if (isSelected()) MaterialTheme.colors.primary.copy(0.8f) else Color.Transparent,
        animationSpec = tween(durationMillis = 200)
    )

    Box(
        modifier = Modifier
            .border(
                BorderStroke(width = 1.5.dp, color = strokeColor),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        SingleLineText(
            modifier = Modifier.noRippleClickable(onSortButtonClick),
            text = text,
            style = MaterialTheme.typography.body2,
        )
    }
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}