package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelperdomain.model.SubCategory

@Composable
fun EditSubCategoryNameDialog(
    getSelectedSubCategory: () -> SubCategory,
    subCategoriesState: State<List<SubCategory>>,
    onPositiveButtonClick: (String, SubCategory) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberEditSubCategoryNameDialogState(
        initialName = getSelectedSubCategory().name,
        subCategoriesState = subCategoriesState
    )
    SubCategoryTextFieldDialog(
        titleRes = R.string.edit_category_name,
        error = state.value.error,
        onDismiss = onDismiss,
        textFieldValueState = state.value.textFieldValueState,
        onPositiveButtonClick = {
            onPositiveButtonClick(
                state.value.text.trim(),
                getSelectedSubCategory()
            )
        },
        positiveButtonEnabled = state.value.positiveButtonEnabled,
        onValueChange = state.value::onValueChange,
        onFocusChanged = state.value::onFocusChange
    )
}

data class EditSubCategoryNameDialogState(
    val initialName: String,
    override val errorState: MutableState<Int?>,
    override val subCategories: List<SubCategory>,
    override val textState: MutableState<String>,
    override val textFieldValueMutableState: MutableState<TextFieldValue>
) : BaseSubCategoryTextFieldDialogState() {

    override fun onValueChange(newTextFiled: TextFieldValue) {
        super.onValueChange(newTextFiled)
        if (newTextFiled.text.trim() == initialName) errorState.value = null
    }

    override val positiveButtonEnabled: Boolean
        get() = super.positiveButtonEnabled && initialName != text.trim()

    fun onFocusChange(newFocusState: FocusState) {
        if (newFocusState.hasFocus)
            textFieldValueMutableState.value =
                textFieldValueMutableState.value.copy(
                    selection = TextRange(0, textFieldValueMutableState.value.text.length)
                )
    }
}

@Composable
fun rememberEditSubCategoryNameDialogState(
    initialName: String,
    errorState: MutableState<Int?> = rememberSaveable { mutableStateOf(null) },
    textState: MutableState<String> = rememberSaveable { mutableStateOf(initialName) },
    textFieldValueState: MutableState<TextFieldValue> = remember {
        mutableStateOf(TextFieldValue(initialName))
    },
    subCategoriesState: State<List<SubCategory>>
) = remember {
    derivedStateOf {
        EditSubCategoryNameDialogState(
            initialName = initialName,
            errorState = errorState,
            subCategories = subCategoriesState.value,
            textState = textState,
            textFieldValueMutableState = textFieldValueState
        )
    }
}