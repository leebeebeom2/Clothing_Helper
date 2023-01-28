package com.leebeebeom.clothinghelper.ui.signin.composable.textfield

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.components.ImeActionRoute
import com.leebeebeom.clothinghelper.ui.components.KeyboardRoute
import com.leebeebeom.clothinghelper.ui.components.MaxWidthTextFieldWithError
import com.leebeebeom.clothinghelper.ui.components.rememberMaxWidthTextFieldState

@Composable
fun EmailTextField(
    error: () -> Int?,
    imeActionRoute: ImeActionRoute = ImeActionRoute.NEXT,
    onInputChange: (String) -> Unit
) {
    val state = rememberEmailTextFieldState(initialError = error, imeActionRoute = imeActionRoute)

    MaxWidthTextFieldWithError(
        state = state,
        onValueChange = state::onValueChange,
        onFocusChanged = state::onFocusChanged,
        onInputChange = onInputChange
    )
}

@Composable
private fun rememberEmailTextFieldState(
    initialError: () -> Int?, imeActionRoute: ImeActionRoute
) = rememberMaxWidthTextFieldState(
    label = R.string.email,
    placeholder = R.string.email_place_holder,
    showKeyboard = true,
    initialError = initialError,
    keyboardRoute = KeyboardRoute.EMAIL,
    imeActionRoute = imeActionRoute,
    blockBlank = true
)