package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.*
import com.leebeebeom.clothinghelperdomain.model.SubCategory

@Composable
fun AddCategoryDialogFab(
    modifier: Modifier, onPositiveButtonClick: (String) -> Unit, subCategories: List<SubCategory>
) {
    val state = rememberAddCategoryDialogUIState()

    FloatingActionButton(
        modifier = modifier.size(48.dp),
        onClick = state::showDialog,
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        SimpleIcon(drawable = R.drawable.ic_add)
    }

    if (state.showDialog) Dialog(onDismissRequest = state::onDismissDialog) {
        DialogRoot {
            DialogTitle(title = R.string.add_category_title)

            DialogTextField(categoryName = state.categoryName,
                error = state.categoryNameError,
                onCategoryNameChange = { state.onCategoryNameChange(it, subCategories) })
            DialogTextButtons(
                positiveButtonEnabled = state.positiveButtonEnabled,
                onPositiveButtonClick = { onPositiveButtonClick(state.categoryName) },
                onDismissDialog = state::onDismissDialog,
            )
            SimpleHeightSpacer(dp = 20)
        }
    }
}

class AddCategoryDialogUIState(
    categoryName: String = "",
    @StringRes categoryNameError: Int? = null,
    showDialog: Boolean = false
) {
    var categoryName by mutableStateOf(categoryName)
        private set
    var categoryNameError by mutableStateOf(categoryNameError)
        private set
    var showDialog by mutableStateOf(showDialog)
        private set

    fun onCategoryNameChange(newName: String, subCategories: List<SubCategory>) {
        this.categoryName = newName
        categoryNameError = null
        if (subCategories.map { it.name }.contains(newName))
            categoryNameError = R.string.error_same_category_name
    }

    val positiveButtonEnabled get() = categoryName.isNotBlank() && categoryNameError == null

    fun showDialog() {
        showDialog = true
    }

    fun onDismissDialog() {
        showDialog = false
        categoryName = ""
        categoryNameError = null
    }

    companion object {
        val Saver: Saver<AddCategoryDialogUIState, *> = listSaver(save = {
            listOf(
                it.categoryName, it.categoryNameError, it.showDialog
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