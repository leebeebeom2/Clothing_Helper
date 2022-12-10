package com.leebeebeom.clothinghelper.ui.main.dialogs

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.util.EditSubCategory
import kotlinx.collections.immutable.ImmutableList

@Composable
fun EditSubCategoryDialog(
    show: () -> Boolean,
    selectedSubCategory: () -> StableSubCategory,
    subCategories: () -> ImmutableList<StableSubCategory>,
    onPositiveButtonClick: EditSubCategory,
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
        onPositiveButtonClick = { onPositiveButtonClick(selectedSubCategory(), it) },
        initialName = { selectedSubCategory().name }
    )
}