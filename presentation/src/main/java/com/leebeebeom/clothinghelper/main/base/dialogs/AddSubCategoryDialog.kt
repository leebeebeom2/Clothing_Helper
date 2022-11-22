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
fun AddSubCategoryDialog(
    subCategories: () -> ImmutableList<StableSubCategory>,
    onPositiveButtonClick: (String) -> Unit,
    show: () -> Boolean,
    onDismiss: () -> Unit
) {
    val names by remember { derivedStateOf { subCategories().map { it.name }.toImmutableList() } }

    AddDialog(
        label = R.string.add_category,
        placeHolder = R.string.category_place_holder,
        title = R.string.add_category,
        names = { names },
        existNameError = R.string.error_exist_category_name,
        onPositiveButtonClick = onPositiveButtonClick,
        show = show,
        onDismiss = onDismiss
    )
}