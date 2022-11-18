package com.leebeebeom.clothinghelper.main.subcategory.content.header

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.CustomIconButton
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.util.getHeaderStringRes
import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.SortPreferences

@Composable
fun SubCategoryHeader(
    parent: SubCategoryParent,
    sort: () -> SortPreferences,
    onSortClick: (Sort) -> Unit,
    onOrderClick: (Order) -> Unit
) {
    SingleLineText(
        text = getHeaderStringRes(parent),
        style = MaterialTheme.typography.h2.copy(fontSize = 32.sp)
    )

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
            modifier = Modifier.size(22.dp),
            onClick = { showDropDownMenu = true },
            drawable = R.drawable.ic_sort,
            tint = LocalContentColor.current.copy(0.5f),
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