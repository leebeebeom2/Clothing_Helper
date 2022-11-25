package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.SimpleIcon
import com.leebeebeom.clothinghelper.main.base.composables.Fab
import com.leebeebeom.clothinghelper.main.base.dialogs.AddSubCategoryDialog
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableList

@Composable
fun BoxScope.SubCategoryFab(
    onPositiveButtonClick: (newName: String) -> Unit,
    subCategories: () -> ImmutableList<StableSubCategory>,
    isSelectMode: () -> Boolean,
    show: () -> Boolean
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    SubCategoryFab(isSelectMode = isSelectMode, show = show, onClick = { showDialog = true })

    AddSubCategoryDialog(
        subCategories = subCategories,
        onPositiveButtonClick = onPositiveButtonClick,
        show = { showDialog },
        onDismiss = { showDialog = false }
    )
}

@Composable
private fun BoxScope.SubCategoryFab(
    onClick: () -> Unit, isSelectMode: () -> Boolean, show: () -> Boolean
) {
    Fab(
        visible = { !isSelectMode() && show() }, onClick = onClick
    ) { SimpleIcon(drawable = R.drawable.ic_add) }
}