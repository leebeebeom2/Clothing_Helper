package com.leebeebeom.clothinghelper.base

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.Anime.Error.errorIn
import com.leebeebeom.clothinghelper.base.Anime.Error.errorOut
import kotlinx.coroutines.delay

@Composable
fun MaxWidthTextField(
    state: MaxWidthTextFieldState,
    textFieldValue: () -> TextFieldValue,
    error: () -> Int? = { null },
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    isVisible: () -> Boolean = { true }
) {
    Column {
        TextField(
            state = state,
            onFocusChanged = onFocusChanged,
            textFieldValue = textFieldValue,
            onValueChange = onValueChange,
            error = error,
            isVisible = isVisible,
            trailingIcon = trailingIcon
        )
        ErrorText(error)
    }

    ShowKeyboard(
        showKeyboardEnabled = { state.showKeyboardEnabled },
        showKeyboard = state::showKeyboard
    )
}

@Composable
private fun TextField(
    state: MaxWidthTextFieldState,
    onFocusChanged: (FocusState) -> Unit,
    textFieldValue: () -> TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    error: () -> Int?,
    isVisible: () -> Boolean,
    trailingIcon: @Composable (() -> Unit)?
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester = state.focusRequester)
            .onFocusChanged(onFocusChanged = onFocusChanged),
        value = textFieldValue(),
        onValueChange = onValueChange,
        label = { Text(text = stringResource(id = state.label), maxLines = 1, overflow = TextOverflow.Ellipsis) },
        placeholder = { Text(text = stringResource(id = state.placeholder), maxLines = 1, overflow = TextOverflow.Ellipsis) },
        isError = error() != null,
        visualTransformation = if (isVisible()) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = state.keyboardOptions,
        trailingIcon = trailingIcon,
        keyboardActions = if (state.keyboardOptions.imeAction == ImeAction.Done) KeyboardActions(
            onDone = { focusManager.clearFocus() }
        )
        else KeyboardActions.Default,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color(0xFFDADADA),
            unfocusedLabelColor = Color(0xFF8391A1),
            backgroundColor = Color(0xFFF7F8F9),
            placeholderColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)
        )
    )
}

@Composable
private fun ErrorText(@StringRes error: () -> Int?) {
    val isError by remember { derivedStateOf { error() != null } }

    AnimatedVisibility(
        visible = isError, enter = errorIn, exit = errorOut
    ) {
        Text(
            modifier = Modifier.padding(start = 4.dp, top = 4.dp),
            text = error()?.let { stringResource(id = it) } ?: "",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
        )
    }
}

data class MaxWidthTextFieldState @OptIn(ExperimentalComposeUiApi::class) constructor(
    @StringRes val label: Int,
    @StringRes val placeholder: Int,
    val showKeyboardEnabled: Boolean,
    val keyboardOptions: KeyboardOptions,
    val focusRequester: FocusRequester,
    val keyboardController: SoftwareKeyboardController?
) {

    @OptIn(ExperimentalComposeUiApi::class)
    suspend fun showKeyboard() {
        focusRequester.requestFocus()
        delay(100)
        keyboardController?.show()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun rememberMaxWidthTextFiledState(
    @StringRes label: Int,
    @StringRes placeholder: Int = R.string.empty,
    showKeyboardEnabled: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done
    ),
    focusRequester: FocusRequester = remember { FocusRequester() },
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
): MaxWidthTextFieldState {
    return remember {
        MaxWidthTextFieldState(
            label = label,
            placeholder = placeholder,
            showKeyboardEnabled = showKeyboardEnabled,
            keyboardOptions = keyboardOptions,
            focusRequester = focusRequester,
            keyboardController = keyboardController
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun rememberEmailTextFieldState(
    imeAction: ImeAction,
): MaxWidthTextFieldState {
    return rememberMaxWidthTextFiledState(
        label = R.string.email,
        placeholder = R.string.email_place_holder,
        showKeyboardEnabled = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = imeAction),
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun rememberPasswordTextFieldState(
    @StringRes label: Int = R.string.password,
    imeAction: ImeAction = ImeAction.Done
): MaxWidthTextFieldState {
    return rememberMaxWidthTextFiledState(
        label = label, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password, imeAction = imeAction
        )
    )
}

@Composable
fun ShowKeyboard(showKeyboardEnabled: () -> Boolean, showKeyboard: suspend () -> Unit) {
    var doesShowKeyboardState by rememberSaveable { mutableStateOf(false) }

    if (!doesShowKeyboardState && showKeyboardEnabled()) {
        LaunchedEffect(key1 = Unit) { showKeyboard() }
        doesShowKeyboardState = true
    }
}