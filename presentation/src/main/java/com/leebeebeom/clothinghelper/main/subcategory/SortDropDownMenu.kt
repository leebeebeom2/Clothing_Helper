package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.SimpleWidthSpacer
import com.leebeebeom.clothinghelperdomain.repository.SortOrder
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySort

@Composable
fun SortDropdownMenu(
    showDropDownMenu: Boolean,
    selectedSort: SubCategorySort,
    selectedOrder: SortOrder,
    onSortClick: (SubCategorySort) -> Unit,
    onOrderClick: (SortOrder) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedSortRes =
        when (selectedSort) {
            SubCategorySort.NAME -> R.string.sort_name
            SubCategorySort.CREATE -> R.string.sort_create_date
        }

    val selectedOrderRes =
        when (selectedOrder) {
            SortOrder.ASCENDING -> R.string.sort_Ascending
            SortOrder.DESCENDING -> R.string.sort_Descending
        }

    MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(20.dp))) {
        DropdownMenu(
            expanded = showDropDownMenu,
            onDismissRequest = onDismiss
        ) {
            Header()
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            ) {

                Column {
                    SortButton(
                        text = R.string.sort_name,
                        selectedRes = selectedSortRes,
                        onSortButtonClick = { onSortClick(SubCategorySort.NAME) }
                    )
                    SimpleHeightSpacer(dp = 8)
                    SortButton(
                        text = R.string.sort_create_date,
                        selectedRes = selectedSortRes,
                        onSortButtonClick = { onSortClick(SubCategorySort.CREATE) }
                    )
                }

                SimpleWidthSpacer(dp = 8)

                Divider(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                )

                SimpleWidthSpacer(dp = 8)

                Column {
                    SortButton(
                        text = R.string.sort_Ascending,
                        selectedRes = selectedOrderRes,
                        onSortButtonClick = { onOrderClick(SortOrder.ASCENDING) }
                    )
                    SimpleHeightSpacer(dp = 8)
                    SortButton(
                        text = R.string.sort_Descending,
                        selectedRes = selectedOrderRes,
                        onSortButtonClick = { onOrderClick(SortOrder.DESCENDING) }
                    )
                }
            }
        }
    }
}

@Composable
private fun Header() {
    Column(modifier = Modifier.padding(start = 12.dp, top = 8.dp)) {
        Text(
            text = stringResource(R.string.sort),
            style = MaterialTheme.typography.subtitle2.copy(letterSpacing = 1.5.sp),
            color = LocalContentColor.current.copy(0.8f)
        )
        SimpleHeightSpacer(dp = 8)
        Divider()
    }
}

@Composable
fun SortButton(
    @StringRes text: Int,
    @StringRes selectedRes: Int,
    onSortButtonClick: () -> Unit
) {
    val strokeColor by animateColorAsState(
        targetValue = if (text == selectedRes) MaterialTheme.colors.primary.copy(0.8f) else Color.Transparent,
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
        Text(
            modifier = Modifier
                .noRippleClickable(onSortButtonClick),
            text = stringResource(id = text),
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