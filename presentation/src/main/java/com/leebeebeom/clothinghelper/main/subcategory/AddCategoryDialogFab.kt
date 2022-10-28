package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

@Composable
fun AddCategoryDialogFab(
    modifier: Modifier,
    onPositiveButtonClick: (String, SubCategoryParent) -> Unit,
    subCategories: List<SubCategory>,
    subCategoryParent: SubCategoryParent
) {
    val state = rememberAddCategoryDialogUIState()

    FloatingActionButton(
        modifier = modifier.size(48.dp),
        onClick = state::showDialog,
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        SimpleIcon(drawable = R.drawable.ic_add)
    }

    if (state.showDialog)
        SubCategoryTextFieldDialog(onDismissDialog = state.onDismissDialog,
            categoryName = state.categoryName,
            error = state.error,
            title = R.string.add_category,
            onCategoryNameChange = { state.onCategoryNameChange(it, subCategories) },
            positiveButtonEnabled = state.positiveButtonEnabled,
            onPositiveButtonClick = {
                onPositiveButtonClick(
                    state.categoryName,
                    subCategoryParent
                )
            })
}

class AddCategoryDialogUIState(
    categoryName: String = "",
    @StringRes categoryNameError: Int? = null,
    showDialog: Boolean = false
) : BaseSubCategoryTextFieldDialogUIState(categoryName, categoryNameError, showDialog) {

    fun showDialog() {
        showDialog = true
    }

    companion object {
        val Saver: Saver<AddCategoryDialogUIState, *> = listSaver(save = {
            listOf(
                it.text, it.error, it.showDialog
            )
        }, restore = {
            AddCategoryDialogUIState(it[0] as String, it[1] as? Int, it[2] as Boolean)
        })
    }
}

@Composable
fun rememberAddCategoryDialogUIState() = rememberSaveable(saver = AddCategoryDialogUIState.Saver) {
    AddCategoryDialogUIState()
}