package com.leebeebeom.clothinghelper.ui.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.ui.util.EditSubCategory
import kotlinx.collections.immutable.ImmutableSet

@Composable // skippable
fun EditSubCategoryDialog(
    show: () -> Boolean,
    selectedSubCategory: () -> SubCategory,
    subCategoryNames: () -> ImmutableSet<String>,
    onPositiveButtonClick: EditSubCategory,
    onDismiss: () -> Unit
) {
    val localSelectedSubCategory by remember { derivedStateOf(selectedSubCategory) }

    if (show()) EditNameDialog(
        label = R.string.category,
        placeHolder = R.string.category_place_holder,
        title = R.string.edit_category,
        existNameError = R.string.error_exist_category_name,
        onDismiss = onDismiss,
        onPositiveButtonClick = { onPositiveButtonClick(localSelectedSubCategory, it) },
        initialName = localSelectedSubCategory.name,
        names = subCategoryNames
    )
}