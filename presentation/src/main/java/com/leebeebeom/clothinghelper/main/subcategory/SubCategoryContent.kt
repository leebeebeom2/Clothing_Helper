package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    mainCategoryName: String,
    allExpandIconClick: () -> Unit,
    allExpand: Boolean,
    subCategories: List<SubCategory>,
    onLongClick: (SubCategory) -> Unit,
    isSelectMode: Boolean,
    onSubCategoryClick: (SubCategory) -> Unit,
    selectedSubCategories: Set<SubCategory>
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
                mainCategoryName = mainCategoryName,
                allExpandIconClick = allExpandIconClick,
                allExpand = allExpand
            )
        }
        items(items = subCategories,
            key = { subCategory -> subCategory.key }) {
            SubCategoryCard(
                subCategory = it,
                onLongClick = { onLongClick(it) },
                isSelectMode = isSelectMode,
                onSubCategoryClick = { onSubCategoryClick(it) },
                allExpand = allExpand,
                isChecked = selectedSubCategories.contains(it)
            )
        }
    }
}

@Composable
private fun Header(
    mainCategoryName: String, allExpandIconClick: () -> Unit, allExpand: Boolean
) {
    Text(
        modifier = Modifier.padding(4.dp),
        text = stringResource(id = getHeaderStringRes(mainCategoryName)),
        style = MaterialTheme.typography.h2,
        fontSize = 32.sp
    )

    // Header Icons
    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier.weight(1f))
        AllExpandIcon(allExpandIconClick = allExpandIconClick, allExpand)
        SortIcon()
    }
}

fun getHeaderStringRes(mainCategoryName: String) =
    when (mainCategoryName) {
        SubCategoryParent.TOP.name -> R.string.top
        SubCategoryParent.BOTTOM.name -> R.string.bottom
        SubCategoryParent.OUTER.name -> R.string.outer
        else -> R.string.etc
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
private fun SortIcon() {
    // TODO 드랍다운 메뉴
    IconButton(onClick = {}, modifier = Modifier.size(22.dp)) {
        SimpleIcon(
            drawable = R.drawable.ic_sort, tint = LocalContentColor.current.copy(0.5f)
        )
    }
}