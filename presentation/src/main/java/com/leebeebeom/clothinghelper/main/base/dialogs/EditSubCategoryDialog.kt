package com.leebeebeom.clothinghelper.main.base.dialogs

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableList

@Composable
fun EditSubCategoryDialog(
    show: () -> Boolean,
    selectedSubCategory: () -> StableSubCategory,
    subCategories: () -> ImmutableList<StableSubCategory>,
    onPositiveButtonClick: (StableSubCategory) -> Unit,
    onDismiss: () -> Unit
) {
    EditDialog(
        label = R.string.category,
        placeHolder = R.string.category_place_holder,
        title = R.string.edit_category,
        show = show,
        items = subCategories,
        existNameError = R.string.error_exist_category_name,
        onDismiss = onDismiss,
        onPositiveButtonClick = { onPositiveButtonClick(selectedSubCategory().copy(name = it)) },
        initialName = { selectedSubCategory().name }
    )
}