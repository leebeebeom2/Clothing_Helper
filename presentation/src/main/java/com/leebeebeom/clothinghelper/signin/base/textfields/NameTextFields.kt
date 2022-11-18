package com.leebeebeom.clothinghelper.signin.base.textfields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NameTextField(
    name: () -> String,
    onNameChange: (String) -> Unit
) {
    val state = remember { NameTextFieldState(name()) }

    MaxWidthTextField(
        textFieldValue = { state.textFieldValue },
        state = rememberMaxWidthTextFieldState(
            label = R.string.name,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        ),
        error = { null },
        onValueChange = { state.onValueChange(it, onNameChange) {} },
        onFocusChanged = state::onFocusChange,
        onCancelIconClick = { state.onCancelIconClick(onNameChange) }
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
