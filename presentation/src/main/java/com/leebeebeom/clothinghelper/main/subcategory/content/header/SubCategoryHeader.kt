package com.leebeebeom.clothinghelper.main.subcategory.content.header

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.CustomIconButton
import com.leebeebeom.clothinghelper.main.base.AllExpandIcon
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.SortOrder
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySort
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySortPreferences

@Composable
fun SubCategoryHeader(
    parent: SubCategoryParent,
    isAllExpanded: () -> Boolean,
    sort: () -> SubCategorySortPreferences,
    allExpandIconClick: () -> Unit,
    onSortClick: (SubCategorySort) -> Unit,
    onOrderClick: (SortOrder) -> Unit
) {
    HeaderText(parent = parent)

    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier.weight(1f))

        AllExpandIcon(
            allExpandIconClick = allExpandIconClick,
            isAllExpanded = isAllExpanded
        )
        SortIcon(
            sort = sort,
            onSortClick = onSortClick,
            onOrderClick = onOrderClick
        )
    }
}

@Composable
fun HeaderText(parent: SubCategoryParent) {
    Text(
        text = stringResource(id = headerStringRes(parent)),
        style = MaterialTheme.typography.h2,
        fontSize = 32.sp
    )
}

@Composable
private fun AllExpandIcon(allExpandIconClick: () -> Unit, isAllExpanded: () -> Boolean) {
    Box(modifier = Modifier.offset(0.dp, 0.dp)) {
        AllExpandIcon(
            size = 22.dp,
            onClick = allExpandIconClick,
            tint = LocalContentColor.current.copy(0.5f),
            isAllExpanded = isAllExpanded
        )
    }
}

@Composable
private fun SortIcon(
    sort: () -> SubCategorySortPreferences,
    onSortClick: (SubCategorySort) -> Unit,
    onOrderClick: (SortOrder) -> Unit
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

fun headerStringRes(parent: SubCategoryParent): Int {
    return when (parent) {
        SubCategoryParent.TOP -> R.string.top
        SubCategoryParent.BOTTOM -> R.string.bottom
        SubCategoryParent.OUTER -> R.string.outer
        SubCategoryParent.ETC -> R.string.etc
    }
}