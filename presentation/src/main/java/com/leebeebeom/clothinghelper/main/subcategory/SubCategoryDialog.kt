package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.runtime.*
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
    textFieldValue: TextFieldValue,
    positiveButtonEnabled: Boolean,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    onPositiveButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {
    DialogRoot(onDismiss = onDismiss) {
        DialogTitle(text = titleRes)

        val maxWidthTextFieldState = rememberMaxWidthTextFiledState(
            textFieldValue = textFieldValue,
            label = R.string.category,
            showKeyboardEnabled = true,
            placeholder = R.string.category_place_holder
        )

        MaxWidthTextField(
            state = maxWidthTextFieldState,
            error = error,
            onValueChange = onValueChange,
            onFocusChanged = onFocusChanged
        )
        SimpleHeightSpacer(dp = 12)

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
    open val positiveButtonEnabled get() = text.isNotBlank() && error == null

    open fun onValueChange(newTextFiled: TextFieldValue) {
        if (textState.value != newTextFiled.text) errorState.value = null
        textState.value = newTextFiled.text
        textFieldValueMutableState.value = newTextFiled
        if (subCategories.map { it.name }.contains(newTextFiled.text)) errorState.value =
            R.string.error_same_category_name
    }
}