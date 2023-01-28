package com.leebeebeom.clothinghelper.ui.signin.composable.textfield

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.components.ImeActionRoute
import com.leebeebeom.clothinghelper.ui.components.MaxWidthTextFieldWithError
import com.leebeebeom.clothinghelper.ui.components.rememberMaxWidthTextFieldState

@Composable
fun NameTextField(
    onInputChange: (String) -> Unit
) {
    val state = rememberMaxWidthTextFieldState(
        label = R.string.nickname,
        imeActionRoute = ImeActionRoute.NEXT
    )

    MaxWidthTextFieldWithError(
        state = state,
        onValueChange = state::onValueChange,
        onFocusChanged = state::onFocusChanged,
        onInputChange = onInputChange
    )
}
