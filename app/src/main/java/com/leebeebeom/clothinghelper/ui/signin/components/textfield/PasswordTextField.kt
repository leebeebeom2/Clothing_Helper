package com.leebeebeom.clothinghelper.ui.signin.components.textfield

import androidx.annotation.StringRes
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.components.*

@Composable
fun PasswordTextField(
    error: () -> Int?,
    imeActionRoute: ImeActionRoute = ImeActionRoute.DONE,
    @StringRes label: Int = R.string.password,
    state: MutableMaxWidthTextFieldState = rememberPasswordTextFieldState(
        imeActionRoute = imeActionRoute,
        label = label
    ),
    onInputChange: (String) -> Unit,
    visibleIconDescription: String = VISIBLE_ICON,
    invisibleIconDescription: String = INVISIBLE_ICON,
) {
    MaxWidthTextFieldWithError(
        state = state,
        onValueChange = state::onValueChange,
        onFocusChanged = state::onFocusChanged,
        onInputChange = onInputChange,
        trailingIcon = {
            VisibleIcon(
                isVisible = { state.isVisible },
                onClick = state::visibilityToggle,
                visibleIconDescription = visibleIconDescription,
                invisibleIconDescription = invisibleIconDescription
            )
        }
    )

    SetTextFieldError(error = error, collect = { state.error = it })
}

const val VISIBLE_ICON = "visible icon"
const val INVISIBLE_ICON = "invisible icon"

@Composable
fun VisibleIcon(
    isVisible: () -> Boolean,
    onClick: () -> Unit,
    visibleIconDescription: String = VISIBLE_ICON,
    invisibleIconDescription: String = INVISIBLE_ICON,
) {
    CustomIconButton(
        drawable = if (isVisible()) R.drawable.ic_visibility_off else R.drawable.ic_visibility,
        tint = LocalContentColor.current.copy(0.4f),
        onClick = onClick,
        size = 24.dp,
        contentDescription = if (isVisible()) invisibleIconDescription else visibleIconDescription
    )
}

@Composable
fun rememberPasswordTextFieldState(
    imeActionRoute: ImeActionRoute,
    label: Int = R.string.password,
) = rememberMaxWidthTextFieldState(
    initialVisibility = false,
    keyboardRoute = KeyboardRoute.PASSWORD,
    imeActionRoute = imeActionRoute,
    label = label,
    cancelIconEnabled = false,
    blockBlank = true
)