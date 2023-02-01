package com.leebeebeom.clothinghelper.ui.signin.composable.textfield

import androidx.annotation.StringRes
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.components.*

@Composable
fun PasswordTextField(
    error: () -> Int?,
    imeActionRoute: ImeActionRoute = ImeActionRoute.DONE,
    @StringRes label: Int = R.string.password,
    onInputChange: (String) -> Unit
) {
    val state = rememberPasswordTextFieldState(imeActionRoute = imeActionRoute, label = label)

    MaxWidthTextFieldWithError(
        state = state,
        onValueChange = state::onValueChange,
        onFocusChanged = state::onFocusChanged,
        onInputChange = onInputChange,
        trailingIcon = {
            VisibleIcon(
                isVisible = { state.isVisible },
                onClick = state::visibilityToggle
            )
        }
    )

    SetTextFieldError(error = error, collect = { state.error = it })
}

const val VISIBLE_ICON = "visible icon"
const val INVISIBLE_ICON = "invisible icon"

@Composable
fun VisibleIcon(isVisible: () -> Boolean, onClick: () -> Unit) {
    CustomIconButton(
        modifier = Modifier.testTag(if (isVisible()) INVISIBLE_ICON else VISIBLE_ICON),
        drawable = if (isVisible()) R.drawable.ic_visibility_off else R.drawable.ic_visibility,
        tint = LocalContentColor.current.copy(0.4f),
        onClick = onClick,
        size = 24.dp
    )
}

@Composable
private fun rememberPasswordTextFieldState(
    imeActionRoute: ImeActionRoute,
    label: Int = R.string.password
) = rememberMaxWidthTextFieldState(
    initialVisibility = false,
    keyboardRoute = KeyboardRoute.PASSWORD,
    imeActionRoute = imeActionRoute,
    label = label,
    cancelIconEnabled = false,
    blockBlank = true
)