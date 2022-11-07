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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.Anime.Error.errorIn
import com.leebeebeom.clothinghelper.base.Anime.Error.errorOut
import kotlinx.coroutines.delay

@Composable
fun MaxWidthTextField(
    state: State<MaxWidthTextFieldState>,
    error: Int? = null,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester = state.value.focusRequester)
                .onFocusChanged(onFocusChanged = onFocusChanged),
            value = state.value.textFieldValue,
            onValueChange = onValueChange,
            label = { Text(text = stringResource(id = state.value.label)) },
            placeholder = { Text(text = stringResource(id = state.value.placeholder)) },
            isError = error != null,
            visualTransformation = visualTransformation,
            singleLine = true,
            maxLines = 1,
            keyboardOptions = state.value.keyboardOptions,
            trailingIcon = trailingIcon,
            keyboardActions = if (state.value.keyboardOptions.imeAction == ImeAction.Done) KeyboardActions(
                onDone = { state.value.clearFocus() })
            else KeyboardActions.Default,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFDADADA),
                unfocusedLabelColor = Color(0xFF8391A1),
                backgroundColor = Color(0xFFF7F8F9),
                placeholderColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)
            )
        )
        ErrorText(error)
    }

    val didShowKeyboardState = rememberSaveable { mutableStateOf(false) }
    if (!didShowKeyboardState.value && state.value.showKeyboardEnabled) {
        LaunchedEffect(key1 = Unit) { state.value.showKeyboard() }
        didShowKeyboardState.value = true
    }
}

@Composable
private fun ErrorText(@StringRes error: Int?) {
    var errorRes by remember { mutableStateOf(error) }
    if (error != null) errorRes = error

    AnimatedVisibility(
        visible = error != null, enter = errorIn, exit = errorOut
    ) {
        Text(
            modifier = Modifier.padding(start = 4.dp, top = 4.dp),
            text = error?.let { stringResource(id = it) } ?: "",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
        )
    }
}

data class MaxWidthTextFieldState @OptIn(ExperimentalComposeUiApi::class) constructor(
    val textFieldValue: TextFieldValue,
    @StringRes val label: Int,
    @StringRes val placeholder: Int,
    val showKeyboardEnabled: Boolean,
    val keyboardOptions: KeyboardOptions,
    override val focusManager: FocusManager,
    override val focusRequester: FocusRequester,
    val keyboardController: SoftwareKeyboardController?
) : ClearFocus, RequestFocus {

    @OptIn(ExperimentalComposeUiApi::class)
    suspend fun showKeyboard() {
        requestFocus()
        delay(100)
        keyboardController?.show()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun rememberMaxWidthTextFiledState(
    textFieldValueState: State<TextFieldValue>,
    @StringRes label: Int,
    @StringRes placeholder: Int = R.string.empty,
    showKeyboardEnabled: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done
    ),
    focusManager: FocusManager = LocalFocusManager.current,
    focusRequester: FocusRequester = remember { FocusRequester() },
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
) = remember {
    derivedStateOf {
        MaxWidthTextFieldState(
            textFieldValue = textFieldValueState.value,
            label = label,
            placeholder = placeholder,
            showKeyboardEnabled = showKeyboardEnabled,
            keyboardOptions = keyboardOptions,
            focusManager = focusManager,
            focusRequester = focusRequester,
            keyboardController = keyboardController
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun rememberEmailTextFieldState(
    textFieldValueState: State<TextFieldValue>,
    imeAction: ImeAction,
) = rememberMaxWidthTextFiledState(
    textFieldValueState = textFieldValueState,
    label = R.string.email,
    placeholder = R.string.email_place_holder,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = imeAction),
    showKeyboardEnabled = false,
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun rememberPasswordTextFieldState(
    textFieldValueState: State<TextFieldValue>,
    @StringRes label: Int = R.string.password,
    imeAction: ImeAction = ImeAction.Done
) = rememberMaxWidthTextFiledState(
    textFieldValueState = textFieldValueState,
    label = label, keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password, imeAction = imeAction
    )
)