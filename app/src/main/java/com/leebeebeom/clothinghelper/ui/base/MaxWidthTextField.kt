package com.leebeebeom.clothinghelper.ui.base

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import kotlinx.coroutines.delay

@Composable
fun MaxWidthTextField(
    modifier: Modifier = Modifier,
    state: TextFieldUIState,
    onValueChange: (String) -> Unit = {},
    trailingIcon: @Composable (() -> Unit)? = null,
    showKeyboardEnabled: Boolean = false
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()

    Column {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = state.text,
            onValueChange = {
                state.text = it
                state.error = TextFieldError.ERROR_OFF
                onValueChange(it)
            },
            label = { Text(text = stringResource(id = state.label)) },
            placeholder = { Text(text = stringResource(id = state.placeHolder)) },
            isError = state.isError,
            visualTransformation = state.visualTransformation,
            singleLine = true,
            maxLines = 1,
            keyboardOptions = state.keyboardOptions,
            trailingIcon = trailingIcon,
            keyboardActions = if (state.imeAction == ImeAction.Done) KeyboardActions(onDone = { focusManager.clearFocus() })
            else KeyboardActions.Default,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFDADADA),
                unfocusedLabelColor = Color(0xFF8391A1),
                backgroundColor = Color(0xFFF7F8F9),
                placeholderColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)
            )
        )
        ErrorText(state)
    }
    if (showKeyboardEnabled) ShowKeyboard(focusRequester)
}


@Composable
private fun ErrorText(state: TextFieldUIState) {
    AnimatedVisibility(
        visible = state.isError,
        enter = expandVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ), exit = shrinkVertically(animationSpec = tween(200))
    ) {
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = stringResource(id = state.error.errorText),
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
        )
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun ShowKeyboard(focusRequester: FocusRequester) {
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        delay(100)
        keyboardController?.show()
    }
}

class TextFieldUIState(
    val imeAction: ImeAction = ImeAction.Done,
    val keyboardType: KeyboardType = KeyboardType.Text,
    @StringRes val label: Int,
    @StringRes val placeHolder: Int = R.string.empty,
    initialVisualTransformation: VisualTransformation = VisualTransformation.None
) {
    var text by mutableStateOf("")
    var error by mutableStateOf(TextFieldError.ERROR_OFF)
    var visualTransformation by mutableStateOf(initialVisualTransformation)

    val visualTransformationToggle = {
        visualTransformation =
            if (isVisible) PasswordVisualTransformation() else VisualTransformation.None
    }

    val keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = keyboardType,
        imeAction = imeAction
    )

    val isBlank get() = text.isBlank()
    val isError get() = error != TextFieldError.ERROR_OFF
    val isVisible get() = visualTransformation == VisualTransformation.None

    companion object {
        fun emailState(imeAction: ImeAction = ImeAction.Done) = TextFieldUIState(
            imeAction = imeAction,
            keyboardType = KeyboardType.Email,
            label = R.string.email,
            placeHolder = R.string.email_place_holder
        )

        fun passwordState(imeAction: ImeAction = ImeAction.Done) = TextFieldUIState(
            imeAction = imeAction,
            keyboardType = KeyboardType.Password,
            label = R.string.password,
            initialVisualTransformation = PasswordVisualTransformation()
        )
    }
}

enum class TextFieldError(@StringRes val errorText: Int) {
    ERROR_OFF(R.string.empty),
    ERROR_WEAK_PASSWORD(R.string.error_weak_password),
    ERROR_PASSWORD_CONFIRM_NOT_SAME(R.string.error_password_confirm_not_same),
    ERROR_INVALID_EMAIL(R.string.error_invalid_email),
    ERROR_USER_NOT_FOUND(R.string.error_user_not_found),
    ERROR_EMAIL_ALREADY_IN_USE(R.string.error_email_already_in_use),
    ERROR_WRONG_PASSWORD(R.string.error_wrong_password),
    ERROR_SAME_CATEGORY_NAME(R.string.error_same_category_name)
}