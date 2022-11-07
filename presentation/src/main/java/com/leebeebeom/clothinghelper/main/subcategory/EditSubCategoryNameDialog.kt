package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelperdomain.model.SubCategory

@Composable
fun EditSubCategoryNameDialog(
    firstSelectedSubCategory: SubCategory,
    subCategories: List<SubCategory>,
    onPositiveButtonClick: (String, SubCategory) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberEditSubCategoryNameDialogState(
        initialName = firstSelectedSubCategory.name,
        subCategories = subCategories
    )
    val initialed = rememberSaveable { mutableStateOf(false) }
    if (!initialed.value) {
        state.setInitialText(firstSelectedSubCategory.name)
        initialed.value = true
    }

    SubCategoryTextFieldDialog(
        titleRes = R.string.edit_category_name,
        error = state.error,
        onDismiss = onDismiss,
        textFieldValueState = state.textFieldValueState,
        onPositiveButtonClick = {
            onPositiveButtonClick(
                state.text.trim(),
                firstSelectedSubCategory
            )
        },
        positiveButtonEnabled = state.positiveButtonEnabled,
        onValueChange = state::onValueChange,
        onFocusChanged = state::onFocusChange
    )
}

data class EditSubCategoryNameDialogState(
    val initialName: String,
    override val errorState: MutableState<Int?>,
    override val subCategories: List<SubCategory>,
    override val textState: MutableState<String>,
    override val textFieldValueMutableState: MutableState<TextFieldValue>,
) : BaseSubCategoryTextFieldDialogState() {
    fun setInitialText(text: String) {
        textState.value = text
        textFieldValueMutableState.value =
            TextFieldValue(text = text, TextRange(0, text.length))
    }

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
    textState: MutableState<String> = rememberSaveable { mutableStateOf("") },
    errorState: MutableState<Int?> = rememberSaveable { mutableStateOf(null) },
    textFieldValueState: MutableState<TextFieldValue> = remember {
        mutableStateOf(TextFieldValue(textState.value))
    },
    subCategories: List<SubCategory>,
) = remember(subCategories) {
    EditSubCategoryNameDialogState(
        initialName = initialName,
        errorState = errorState,
        subCategories = subCategories,
        textState = textState,
        textFieldValueMutableState = textFieldValueState,
    )
}