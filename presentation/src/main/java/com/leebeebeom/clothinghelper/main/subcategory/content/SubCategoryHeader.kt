package com.leebeebeom.clothinghelper.main.subcategory.content

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.main.base.composables.sort.SortIconWithDivider
import com.leebeebeom.clothinghelper.util.getHeaderStringRes
import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.SortPreferences
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent

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

    SortIconWithDivider(sort, onSortClick, onOrderClick)
}