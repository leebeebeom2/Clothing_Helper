package com.leebeebeom.clothinghelper.ui.signin.components.textfield

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.components.*

@Composable
fun EmailTextField(
    error: () -> Int?,
    imeActionRoute: ImeActionRoute = ImeActionRoute.NEXT,
    onInputChange: (String) -> Unit
) {
    val state = rememberEmailTextFieldState(imeActionRoute = imeActionRoute)

    MaxWidthTextFieldWithError(
        state = state,
        onValueChange = state::onValueChange,
        onFocusChanged = state::onFocusChanged,
        onInputChange = onInputChange
    )

    SetTextFieldError(error = error, collect = { state.error = it })
}

@Composable
private fun rememberEmailTextFieldState(
    imeActionRoute: ImeActionRoute
) = rememberMaxWidthTextFieldState(
    label = R.string.email,
    placeholder = R.string.email_place_holder,
    showKeyboard = true,
    keyboardRoute = KeyboardRoute.EMAIL,
    imeActionRoute = imeActionRoute,
    blockBlank = true
)