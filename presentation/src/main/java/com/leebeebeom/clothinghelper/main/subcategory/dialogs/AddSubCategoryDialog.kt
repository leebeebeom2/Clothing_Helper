package com.leebeebeom.clothinghelper.main.subcategory.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.dialogs.AddDialog
import com.leebeebeom.clothinghelper.base.dialogs.AddDialogState
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun AddSubCategoryDialog(
    state: AddDialogState,
    subCategories: () -> ImmutableList<StableSubCategory>,
    onPositiveButtonClick: (String) -> Unit,
) {
    val names by remember { derivedStateOf { subCategories().map { it.name }.toImmutableList() } }

    AddDialog(
        label = R.string.add_category,
        placeHolder = R.string.category_place_holder,
        title = R.string.add_category,
        state = state,
        names = { names },
        existNameError = R.string.error_exist_category_name,
        onPositiveButtonClick = onPositiveButtonClick
    )
}