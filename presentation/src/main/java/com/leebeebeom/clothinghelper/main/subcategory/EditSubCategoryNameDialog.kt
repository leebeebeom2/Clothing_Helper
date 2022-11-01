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
    getSelectedCategoryName: () -> String,
    state: EditSubCategoryNameDialogState,
    subCategories: List<SubCategory>,
    onPositiveButtonClick: (String) -> Unit,
) {

    if (state.showDialog) {
        state.setInitialName(getSelectedCategoryName())

        SubCategoryTextFieldDialog(
            state = state,
            onPositiveButtonClick = { onPositiveButtonClick(state.textFiled.text) },
            subCategories = subCategories
        )
    }
}

class EditSubCategoryNameDialogState(
    text: String = "", error: Int? = null, showDialog: Boolean = false, initialName: String = ""
) : BaseSubCategoryTextFieldDialogState(
    text = text,
    error = error,
    showDialog = showDialog,
    title = R.string.edit_category_name
) {
    var initialName: String = initialName
        private set

    fun setInitialName(name: String) {
        textFiled = TextFieldValue(name)
        initialName = name
    }

    override fun onFocusChanged(focusState: FocusState) {
        if (focusState.hasFocus)
            textFiled = textFiled.copy(selection = TextRange(0, textFiled.text.length))
    }

    override val positiveButtonEnabled: Boolean
        get() = super.positiveButtonEnabled && initialName != textFiled.text

    companion object {
        val Saver: Saver<EditSubCategoryNameDialogState, *> = listSaver(save = {
            listOf(
                it.textFiled.text, it.error, it.showDialog, it.initialName
            )
        }, restore = {
            EditSubCategoryNameDialogState(
                it[0] as String, it[1] as? Int, it[2] as Boolean, it[3] as String
            )
        })
    }
}

@Composable
fun rememberEditSubCategoryNameDialogState() =
    rememberSaveable(saver = EditSubCategoryNameDialogState.Saver) {
        EditSubCategoryNameDialogState()
    }