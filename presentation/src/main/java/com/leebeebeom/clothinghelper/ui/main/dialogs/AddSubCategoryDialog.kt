package com.leebeebeom.clothinghelper.ui.main.dialogs

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AddSubCategoryDialog(
    subCategories: () -> ImmutableList<StableSubCategory>,
    onPositiveButtonClick: (String) -> Unit,
    show: () -> Boolean,
    onDismiss: () -> Unit
) {
    AddDialog(
        label = R.string.category,
        placeHolder = R.string.category_place_holder,
        title = R.string.add_category,
        items = subCategories,
        existNameError = R.string.error_exist_category_name,
        onPositiveButtonClick = onPositiveButtonClick,
        show = show,
        onDismiss = onDismiss
    )
}