package com.leebeebeom.clothinghelper.main.base.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun EditSubCategoryDialog(
    show: () -> Boolean,
    subCategory: () -> StableSubCategory,
    subCategories: () -> ImmutableList<StableSubCategory>,
    onPositiveButtonClick: (StableSubCategory) -> Unit,
    onDismiss: () -> Unit
) {
    val names by remember {
        derivedStateOf { subCategories().map { it.name }.toImmutableList() }
    }

    EditDialog(
        label = R.string.edit_category,
        placeHolder = R.string.category_place_holder,
        title = R.string.edit_category,
        show = show,
        names = { names },
        existNameError = R.string.error_exist_category_name,
        onDismiss = onDismiss,
        onPositiveButtonClick = { onPositiveButtonClick(subCategory().copy(name = it)) },
        initialName = { subCategory().name }
    )
}