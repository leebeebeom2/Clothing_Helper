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
            val state = rememberSaveable(saver = EditSubCategoryNameDialogState.Saver) {
                EditSubCategoryNameDialogState(selectedSubCategory.name)
            }

            SubCategoryTextFieldDialog(
                showDialog = showDialog,
                title = R.string.edit_category_name,
                error = { state.error },
                onDismiss = onDismiss,
                textFieldValue = { state.textFieldValue },
                onPositiveButtonClick = { onPositiveButtonClick(selectedSubCategory.copy(name = state.text.trim())) },
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
}

class EditSubCategoryNameDialogState(
    val initialName: String,
    initialText: String = initialName,
    initialError: Int? = null
) : BaseSubCategoryDialogState(initialText, initialError) {

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
            save = { listOf(it.initialName, it.text, it.error) },
            restore = {
                EditSubCategoryNameDialogState(
                    it[0] as String,
                    it[1] as String,
                    it[2] as? Int
                )
            }
        )
    }
}