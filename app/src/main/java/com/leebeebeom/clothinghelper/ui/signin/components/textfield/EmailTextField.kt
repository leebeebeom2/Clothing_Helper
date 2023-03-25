package com.leebeebeom.clothinghelper.ui.signin.components.textfield

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.components.StatefulMaxWidthTestFieldWithCancelIcon

@Composable
fun EmailTextField(
    initialEmail: String,
    error: () -> Int?,
    imeAction: ImeAction = ImeAction.Next,
    onEmailChange: (String) -> Unit,
) {
    StatefulMaxWidthTestFieldWithCancelIcon(
        initialText = initialEmail,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = imeAction),
        onInputChange = onEmailChange,
        label = R.string.email,
        placeholder = R.string.email_place_holder,
        blockBlank = true,
        error = error,
        getFocus = true
    )
}