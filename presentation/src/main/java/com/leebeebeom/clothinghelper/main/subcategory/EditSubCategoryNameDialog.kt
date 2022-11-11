package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.R
import kotlinx.collections.immutable.ImmutableList

@Composable
fun EditSubCategoryNameDialog(
    showDialog: () -> Boolean,
    selectedSubCategoryName: () -> String,
    subCategoryNames: () -> ImmutableList<String>,
    onPositiveButtonClick: (name: String) -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog()) {
        val state = rememberSaveable(saver = EditSubCategoryNameDialogState.Saver) {
            EditSubCategoryNameDialogState(selectedSubCategoryName())
        }

        SubCategoryTextFieldDialog(
            showDialog = showDialog,
            title = R.string.edit_category_name,
            error = { state.error },
            onDismiss = onDismiss,
            textFieldValue = { state.textFieldValue },
            onPositiveButtonClick = { onPositiveButtonClick(state.text) },
            positiveButtonEnabled = { state.positiveButtonEnabled },
            onValueChange = {
                state.onValueChange(it)
                if (subCategoryNames().contains(it.text.trim())) state.updateError(R.string.error_same_category_name)
                if (state.initialName == it.text) state.updateError(null)
            },
            onFocusChanged = state::onFocusChange
        )
    }
}

class EditSubCategoryNameDialogState(val initialName: String, initialError: Int? = null) :
    BaseSubCategoryDialogState(initialName, initialError) {

    override fun onValueChange(newTextFiled: TextFieldValue) {
        super.onValueChange(newTextFiled)
        if (newTextFiled.text.trim() == initialName) error = null
    }

    override val positiveButtonEnabled by derivedStateOf { super.positiveButtonEnabled && initialName != text.trim() }

    fun onFocusChange(newFocusState: FocusState) {
        if (newFocusState.hasFocus)
            textFieldValue = textFieldValue.copy(selection = TextRange(0, text.length))
    }

    companion object {
        val Saver: Saver<EditSubCategoryNameDialogState, *> = listSaver(
            save = { listOf(it.text, it.error) },
            restore = { EditSubCategoryNameDialogState(it[0] as String, it[1] as? Int) }
        )
    }
}