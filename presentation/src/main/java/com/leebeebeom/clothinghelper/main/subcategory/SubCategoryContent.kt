package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

@Composable
fun SubCategoryContent(
    subCategoryParent: SubCategoryParent,
    allExpandIconClick: () -> Unit,
    allExpand: Boolean,
    subCategories: List<SubCategory>,
    getExpandState: (index: Int) -> Boolean,
    toggleExpand: (index: Int) -> Unit,
    onLongClick: () -> Unit,
    isSelectMode: Boolean,
    onSelect: (SubCategory, isChecked:Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp, end = 16.dp, top = 16.dp, bottom = 120.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Header(
                subCategoryParent = subCategoryParent,
                allExpandIconClick = allExpandIconClick,
                allExpand = allExpand
            )
        }
        itemsIndexed(items = subCategories,
            key = { _, subCategory -> subCategory.key }) { index, subCategory ->
            SubCategoryCard(
                subCategory = subCategory,
                isExpanded = getExpandState(index),
                onExpandIconClick = { toggleExpand(index) },
                onLongClick = onLongClick,
                isSelectMode = isSelectMode,
                onSelect = onSelect
            )
        }
    }
}

@Composable
private fun Header(
    subCategoryParent: SubCategoryParent, allExpandIconClick: () -> Unit, allExpand: Boolean
) {
    // Header Text
    Text(
        modifier = Modifier.padding(4.dp),
        text = stringResource(id = getHeaderTextRes(subCategoryParent)),
        style = MaterialTheme.typography.h2,
        fontSize = 32.sp
    )

    // Header Icons
    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier.weight(1f))
        AllExpandIcon(allExpandIconClick = allExpandIconClick, allExpand)
        SortIcon {/*TODO*/ }
    }
}

private fun getHeaderTextRes(subCategoryParent: SubCategoryParent): Int {
    return when (subCategoryParent) {
        SubCategoryParent.TOP -> R.string.top
        SubCategoryParent.BOTTOM -> R.string.bottom
        SubCategoryParent.OUTER -> R.string.outer
        SubCategoryParent.ETC -> R.string.etc
    }
}

@Composable
private fun AllExpandIcon(allExpandIconClick: () -> Unit, allExpand: Boolean) {
    IconButton(
        onClick = allExpandIconClick, modifier = Modifier.size(22.dp)
    ) {
        SimpleIcon(
            drawable = if (allExpand) R.drawable.ic_unfold_less else R.drawable.ic_all_expand,
            tint = LocalContentColor.current.copy(0.5f)
        )
    }
}

@Composable
private fun SortIcon(sortIconClick: () -> Unit) {
    IconButton(onClick = sortIconClick, modifier = Modifier.size(22.dp)) {
        SimpleIcon(
            drawable = R.drawable.ic_sort, tint = LocalContentColor.current.copy(0.5f)
        )
    }
}