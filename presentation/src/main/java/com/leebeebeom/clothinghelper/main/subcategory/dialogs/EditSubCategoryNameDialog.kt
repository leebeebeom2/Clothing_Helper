package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.dialogs.EditDialog
import com.leebeebeom.clothinghelper.base.dialogs.EditDialogState
import com.leebeebeom.clothinghelper.map.StableSubCategory
import kotlinx.collections.immutable.ImmutableList

@Composable
fun EditSubCategoryNameDialog(
    showDialog: () -> Boolean,
    subCategory: () -> StableSubCategory?,
    subCategoryNames: () -> ImmutableList<String>,
    onPositiveButtonClick: (StableSubCategory) -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog()) {
        subCategory()?.let { selectedSubCategory ->
            val state = rememberSaveable(saver = EditDialogState.Saver) {
                EditDialogState(selectedSubCategory.name)
            }

            EditDialog(
                state = state,
                label = R.string.add_category,
                placeHolder = R.string.category_place_holder,
                showDialog = showDialog,
                title = R.string.edit_category_name,
                onDismiss = onDismiss,
                onPositiveButtonClick = { onPositiveButtonClick(selectedSubCategory.copy(name = state.text.trim())) },
                names = subCategoryNames,
                existNameError = R.string.error_exist_category_name
            )
        }
    }
}