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
import androidx.compose.ui.window.Dialog
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.*
import com.leebeebeom.clothinghelper.signin.base.BaseSubCategoryTextFieldDialogUIState
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

            DialogTextField(categoryName = state.text,
                error = state.error,
                onCategoryNameChange = { state.onTextChange(it, subCategories) })
            DialogTextButtons(
                positiveButtonEnabled = state.positiveButtonEnabled,
                onPositiveButtonClick = { onPositiveButtonClick(state.text) },
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