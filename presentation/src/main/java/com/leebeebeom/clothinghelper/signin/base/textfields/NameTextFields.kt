package com.leebeebeom.clothinghelper.signin.base.textfields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.*
import com.leebeebeom.clothinghelper.base.composables.MaxWidthTextField
import com.leebeebeom.clothinghelper.base.composables.rememberMaxWidthTextFieldState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NameTextField(
    name: () -> String,
    onNameChange: (String) -> Unit
) {
    val state = remember { NameTextFieldState(name()) }

    MaxWidthTextField(
        state = rememberMaxWidthTextFieldState(
            label = R.string.name,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        ),
        textFieldValue = { state.textFieldValue },
        error = { null },
        onValueChange = { state.onValueChange(it, onNameChange) {} },
        onFocusChanged = state::onFocusChange
    )
}

class NameTextFieldState(name: String) : TextFieldState(name) {
    override fun onValueChange(
        newTextFieldValue: TextFieldValue,
        onValueChange: (String) -> Unit,
        updateError: (Int?) -> Unit
    ) {
        textFieldValue = newTextFieldValue
        onValueChange(newTextFieldValue.text)
    }
}
