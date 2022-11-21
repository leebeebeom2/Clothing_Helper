package com.leebeebeom.clothinghelper.main.base.composables.sort

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.CustomIconButton
import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.SortPreferences

@Composable
fun SortIconWithDivider(
    sort: () -> SortPreferences,
    onSortClick: (Sort) -> Unit,
    onOrderClick: (Order) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier.weight(1f))

        SortIcon(
            sort = sort,
            onSortClick = onSortClick,
            onOrderClick = onOrderClick
        )
    }
}

@Composable
private fun SortIcon(
    sort: () -> SortPreferences,
    onSortClick: (Sort) -> Unit,
    onOrderClick: (Order) -> Unit
) {
    var showDropDownMenu by remember { mutableStateOf(false) }

    Box {
        CustomIconButton(
            onClick = { showDropDownMenu = true },
            drawable = R.drawable.ic_sort,
            tint = LocalContentColor.current.copy(0.5f),
            size = 22.dp
        )

        SortDropdownMenu(
            show = { showDropDownMenu },
            sort = sort,
            onSortClick = onSortClick,
            onOrderClick = onOrderClick,
            onDismiss = { showDropDownMenu = false }
        )
    }
}