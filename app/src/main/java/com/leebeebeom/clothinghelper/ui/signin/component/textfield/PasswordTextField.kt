package com.leebeebeom.clothinghelper.ui.signin.component.textfield

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.CustomIconButton
import com.leebeebeom.clothinghelper.ui.component.StatefulMaxWidthTextField
import com.leebeebeom.clothinghelper.ui.component.rememberMaxWidthTestFieldState

@Composable
fun PasswordTextField(
    @StringRes label: Int = R.string.password,
    error: () -> Int?,
    imeAction: ImeAction,
    onInputChange: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    var isVisible by rememberSaveable { mutableStateOf(false) }

    StatefulMaxWidthTextField(
        state = rememberMaxWidthTestFieldState(blockBlank = true),
        label = label,
        error = error,
        isVisible = { isVisible },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password, imeAction = imeAction
        ),
        onInputChange = onInputChange,
        focusRequester = focusRequester,
        trailingIcon = {
            VisibleIcon(isVisible = { isVisible }) {
                focusRequester.requestFocus()
                isVisible = !isVisible
            }
        }
    )
}

const val VisibleIconTag = "visible icon"
const val InvisibleIconTag = "invisible icon"

@Composable
fun VisibleIcon(
    isVisible: () -> Boolean,
    onClick: () -> Unit
) {
    CustomIconButton(
        modifier = Modifier.testTag(if (isVisible()) InvisibleIconTag else VisibleIconTag),
        drawable = if (isVisible()) R.drawable.ic_visibility_off else R.drawable.ic_visibility,
        tint = LocalContentColor.current.copy(0.4f),
        onClick = onClick,
        size = 24.dp
    )
}