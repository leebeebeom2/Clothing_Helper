package com.leebeebeom.clothinghelper.ui.signin.components.textfield

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.components.CustomIconButton
import com.leebeebeom.clothinghelper.ui.components.StatefulMaxWidthTestField

@Composable
fun PasswordTextField(
    initialPassword: String,
    error: () -> Int?,
    imeAction: ImeAction,
    onInputChange: (String) -> Unit,
) {
    var isVisible by rememberSaveable { mutableStateOf(false) }

    StatefulMaxWidthTestField(initialText = initialPassword,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password, imeAction = imeAction
        ),
        onInputChange = onInputChange,
        blockBlank = true,
        error = error,
        label = R.string.password,
        isVisible = { isVisible },
        trailingIcon = { focusRequester ->
            VisibleIcon(isVisible = { isVisible }, onClick = {
                focusRequester.requestFocus()
                isVisible = !isVisible
            })
        })
}

const val visibleIconDescription = "visible icon"
const val InvisibleIconDescription = "invisible icon"

@Composable
fun VisibleIcon(
    isVisible: () -> Boolean,
    onClick: () -> Unit,
    visibleIconDescription: String = com.leebeebeom.clothinghelper.ui.signin.components.textfield.visibleIconDescription,
    invisibleIconDescription: String = InvisibleIconDescription,
) {
    CustomIconButton(
        drawable = if (isVisible()) R.drawable.ic_visibility_off else R.drawable.ic_visibility,
        tint = LocalContentColor.current.copy(0.4f),
        onClick = onClick,
        size = 24.dp,
        contentDescription = if (isVisible()) invisibleIconDescription else visibleIconDescription
    )
}