package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelperdomain.model.SubCategory

@Composable
fun EditSubCategoryNameDialog(
    initialCategoryName: String,
    state: EditSubCategoryNameDialogState,
    subCategories: List<SubCategory>,
    onPositiveButtonClick: (String) -> Unit,
) {
    state.onValueChange(TextFieldValue(initialCategoryName))

    if (state.showDialog)
        SubCategoryTextFieldDialog(
            state = state,
            onPositiveButtonClick = { onPositiveButtonClick(state.textFiled.text) },
            subCategories = subCategories
        )
}

class EditSubCategoryNameDialogState(
    private val initialCategoryName: String = "", error: Int? = null, showDialog: Boolean = false
) : BaseSubCategoryTextFieldDialogState(
    initialCategoryName,
    error,
    showDialog,
    R.string.edit_category_name
) {
    override fun onFocusChanged(focusState: FocusState) {
        if (focusState.hasFocus)
            textFiled = textFiled.copy(selection = TextRange(0, textFiled.text.length))
    }

    override val positiveButtonEnabled: Boolean
        get() = super.positiveButtonEnabled && initialCategoryName != textFiled.text

    companion object {
        val Saver: Saver<EditSubCategoryNameDialogState, *> = listSaver(save = {
            listOf(
                it.textFiled.text, it.error, it.showDialog
            )
        }, restore = {
            EditSubCategoryNameDialogState(
                it[0] as String, it[1] as? Int, it[2] as Boolean
            )
        })
    }
}

@Composable
fun rememberEditSubCategoryNameDialogState(initialCategoryName: String = "") =
    rememberSaveable(saver = EditSubCategoryNameDialogState.Saver) {
        EditSubCategoryNameDialogState(initialCategoryName)
    }