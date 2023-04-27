package com.leebeebeom.clothinghelper.ui.signin.component.textfield

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.StatefulMaxWidthTextFieldWithCancelIcon
import com.leebeebeom.clothinghelper.ui.component.rememberMaxWidthTestFieldState

@Composable
fun EmailTextField(
    error: () -> Int?,
    imeAction: ImeAction = ImeAction.Next,
    onEmailChange: (String) -> Unit,
) {
    val state = rememberMaxWidthTestFieldState(blockBlank = true)

    StatefulMaxWidthTextFieldWithCancelIcon(
        state = state,
        label = R.string.email,
        placeholder = R.string.email_place_holder,
        error = error,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = imeAction),
        onInputChange = onEmailChange,
        getFocus = true,
        onValueChange = state::onValueChange,
        onFocusChanged = state::onFocusChanged
    )
}