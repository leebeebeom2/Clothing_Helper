package com.leebeebeom.clothinghelper.ui.signin.composable.textfield

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.components.ImeActionRoute
import com.leebeebeom.clothinghelper.ui.components.KeyboardRoute
import com.leebeebeom.clothinghelper.ui.components.MaxWidthTextFieldWithError
import com.leebeebeom.clothinghelper.ui.components.rememberMaxWidthTextFieldState
import com.leebeebeom.clothinghelper.ui.signin.composable.VisibleIcon

@Composable
fun PasswordTextField(
    error: () -> Int?,
    imeActionRoute: ImeActionRoute = ImeActionRoute.DONE,
    @StringRes label: Int = R.string.password,
    onPasswordChange: (String) -> Unit
) {
    val state = rememberPasswordTextFieldState(initialError = error(), imeActionRoute, label)

    MaxWidthTextFieldWithError(
        state = state,
        onValueChange = state::onValueChange,
        onFocusChanged = state::onFocusChanged,
        trailingIcon = {
            VisibleIcon(
                isVisible = { state.isVisible },
                onClick = state::visibleToggle
            )
        })

    LaunchedEffect(state) { snapshotFlow { state.textFieldValue.text }.collect(onPasswordChange) }
}

@Composable
private fun rememberPasswordTextFieldState(
    initialError: Int?,
    imeActionRoute: ImeActionRoute = ImeActionRoute.DONE,
    label: Int = R.string.password
) = rememberMaxWidthTextFieldState(
    initialError = initialError,
    initialVisibility = false,
    keyboardRoute = KeyboardRoute.PASSWORD,
    imeActionRoute = imeActionRoute,
    label = label,
    cancelIconEnabled = false
)