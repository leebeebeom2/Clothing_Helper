package com.leebeebeom.clothinghelper.ui.signin.components.textfield

import androidx.annotation.StringRes
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

const val VISIBLE_ICON_TAG = "visible icon"
const val INVISIBLE_ICON_TAG = "invisible icon"

@Composable
fun VisibleIcon(
    modifier: Modifier = Modifier,
    isVisible: () -> Boolean,
    onClick: () -> Unit
) {
    CustomIconButton(
        modifier = modifier,
        drawable = if (isVisible()) R.drawable.ic_visibility_off else R.drawable.ic_visibility,
        tint = LocalContentColor.current.copy(0.4f),
        onClick = onClick,
        size = 24.dp,
        contentDescription = if (isVisible()) INVISIBLE_ICON_TAG else VISIBLE_ICON_TAG
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