package com.leebeebeom.clothinghelper.ui.signin.composable.textfield

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.text.input.ImeAction
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.composable.MaxWidthTextField
import com.leebeebeom.clothinghelper.composable.rememberMaxWidthTextFieldState
import com.leebeebeom.clothinghelper.state.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NameTextField(
    initialName: () -> String,
    onNameChange: (String) -> Unit
) {
    val state = remember { TextFieldState(initialName()) }

    MaxWidthTextField(
        state = rememberMaxWidthTextFieldState(
            label = R.string.nickname,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        ),
        textFieldValue = { state.textFieldValue },
        error = { null },
        onValueChange = {
            state.onValueChange(
                newTextFieldValue = it,
                onValueChange = onNameChange,
                updateError = {}
            )
        },
        onFocusChanged = state::onFocusChange
    )
}
