package com.leebeebeom.clothinghelper.main.subcategory.header

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
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
import com.leebeebeom.clothinghelper.main.subcategory.SubCategoryContentState
import com.leebeebeom.clothinghelper.main.subcategory.getHeaderStringRes
import com.leebeebeom.clothinghelperdomain.repository.SortOrder
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySort
import com.leebeebeom.clothinghelperdomain.repository.SubCategorySortPreferences

@Composable
fun SubCategoryHeader(
    state: SubCategoryHeaderState,
    allExpandIconClick: () -> Unit,
    onSortClick: (SubCategorySort) -> Unit,
    onOrderClick: (SortOrder) -> Unit
) {
    Text(
        modifier = Modifier.padding(4.dp),
        text = stringResource(id = state.headerText),
        style = MaterialTheme.typography.h2,
        fontSize = 32.sp
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier.weight(1f))

        AllExpandIcon(
            allExpandIconClick = allExpandIconClick,
            allExpand = state.isAllExpand
        )
        SortIcon(
            sort = state.sort,
            onSortClick = onSortClick,
            onOrderClick = onOrderClick
        )
    }
}

@Composable
private fun AllExpandIcon(allExpandIconClick: () -> Unit, allExpand: Boolean) {
    Box(modifier = Modifier.offset(4.dp, 0.dp)) {
        AllExpandIcon(
            size = 22.dp,
            allExpandIconClick = allExpandIconClick,
            tint = LocalContentColor.current.copy(0.5f),
            allExpand = allExpand,
            rippleSize = 2.dp
        )
    }
}

@Composable
private fun SortIcon(
    sort: SubCategorySortPreferences,
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
            rippleSize = 2.dp
        )

        SortDropdownMenu(
            showDropDownMenu = showDropDownMenu,
            sort = sort,
            onSortClick = onSortClick,
            onOrderClick = onOrderClick,
            onDismiss = { showDropDownMenu = false }
        )
    }
}

data class SubCategoryHeaderState(
    @StringRes val headerText: Int,
    val isAllExpand: Boolean,
    val sort: SubCategorySortPreferences,
)

@Composable
fun rememberSubCategoryHeaderState(
    subCategoryContentState: SubCategoryContentState
) = remember {
    derivedStateOf {
        SubCategoryHeaderState(
            headerText = getHeaderStringRes(subCategoryContentState.parent) ,
            isAllExpand = subCategoryContentState.isAllExpand,
            sort = subCategoryContentState.sort
        )
    }
}