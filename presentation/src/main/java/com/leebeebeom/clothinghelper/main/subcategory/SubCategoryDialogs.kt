package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.*
import com.leebeebeom.clothinghelperdomain.model.SubCategory

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SubCategoryTextFieldDialog(
    @StringRes titleRes: Int,
    error: Int?,
    textFieldValueState: State<TextFieldValue>,
    positiveButtonEnabled: Boolean,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    onPositiveButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {

    val maxWidthTextFieldState =
        rememberMaxWidthTextFiledState(
            textFieldValueState = textFieldValueState,
            label = R.string.category,
            showKeyboardEnabled = true,
            placeholder = R.string.category_place_holder
        )

    DialogRoot(onDismiss = onDismiss) {
        DialogTitle(text = titleRes)

        DialogTextField(
            state = maxWidthTextFieldState.value,
            error = error,
            onValueChange = onValueChange,
            onFocusChanged = onFocusChanged
        )
        DialogTextButtons(
            positiveButtonEnabled = positiveButtonEnabled,
            onPositiveButtonClick = onPositiveButtonClick,
            onDismiss = onDismiss
        )
    }
}

abstract class BaseSubCategoryTextFieldDialogState {
    protected abstract val textState: MutableState<String>
    protected abstract val textFieldValueMutableState: MutableState<TextFieldValue>
    protected abstract val errorState: MutableState<Int?>
    protected abstract val subCategories: List<SubCategory>

    val text get() = textState.value
    val textFieldValueState: State<TextFieldValue> get() = textFieldValueMutableState
    val error get() = errorState.value
    val positiveButtonEnabled get() = text.isNotBlank() && error == null

    open fun onValueChange(newTextFiled: TextFieldValue) {
        if (textState.value != newTextFiled.text) errorState.value = null
        textState.value = newTextFiled.text
        textFieldValueMutableState.value = newTextFiled
        if (subCategories.map { it.name }.contains(newTextFiled.text))
            errorState.value = R.string.error_same_category_name
    }
}