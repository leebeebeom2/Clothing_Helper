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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
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
    maxWidthTextFieldStateHolder: MaxWidthTextFieldStateHolder,
    error: Int? = null,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState) -> Unit = {},
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester = maxWidthTextFieldStateHolder.focusRequester)
                .onFocusChanged(onFocusChanged = onFocusChanged),
            value = maxWidthTextFieldStateHolder.textFieldState,
            onValueChange = onValueChange,
            label = { Text(text = stringResource(id = maxWidthTextFieldStateHolder.label)) },
            placeholder = { Text(text = stringResource(id = maxWidthTextFieldStateHolder.placeholder)) },
            isError = error != null,
            visualTransformation = visualTransformation,
            singleLine = true,
            maxLines = 1,
            keyboardOptions = maxWidthTextFieldStateHolder.keyboardOptions,
            trailingIcon = trailingIcon,
            keyboardActions =
            if (maxWidthTextFieldStateHolder.keyboardOptions.imeAction == ImeAction.Done)
                KeyboardActions(onDone = { maxWidthTextFieldStateHolder.onKeyBoardActionDoneClick() })
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
    if (maxWidthTextFieldStateHolder.showKeyboardEnabled) ShowKeyboard(maxWidthTextFieldStateHolder.focusRequester)
}

@Composable
private fun ErrorText(@StringRes error: Int?) {
    var errorRes by remember { mutableStateOf(error) }
    if (error != null) errorRes = error

    AnimatedVisibility(
        visible = error != null,
        enter = errorIn,
        exit = errorOut
    ) {
        Text(
            modifier = Modifier.padding(start = 4.dp, top = 4.dp),
            text = error?.let { stringResource(id = it) } ?: "",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShowKeyboard(focusRequester: FocusRequester) {
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        delay(100)
        keyboardController?.show()
    }
}

data class MaxWidthTextFieldStateHolder(
    private val _textFieldState: MutableState<TextFieldValue>,
    @StringRes val label: Int,
    @StringRes val placeholder: Int,
    val showKeyboardEnabled: Boolean,
    val keyboardOptions: KeyboardOptions,
    val focusManager: FocusManager,
    val focusRequester: FocusRequester
) {
    val textState get() = _textFieldState.value.text
    val textFieldState get() = _textFieldState.value

    fun onKeyBoardActionDoneClick() {
        focusManager.clearFocus()
    }

    fun onValueChange(newText: TextFieldValue, updateError: (Int?) -> Unit) {
        if (textState != newText.text) updateError(null)
        _textFieldState.value = newText
    }

    fun onFocusChanged(focusState: FocusState) {
        if (focusState.hasFocus)
            _textFieldState.value = _textFieldState.value.copy(selection = TextRange(textState.length))
    }
}

@Composable
fun rememberMaxWidthTextFiledState(
    textField: MutableState<TextFieldValue> = rememberSaveable { mutableStateOf(TextFieldValue("")) },
    @StringRes label: Int,
    @StringRes placeholder: Int = R.string.empty,
    showKeyboardEnabled: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done
    ),
    focusManager: FocusManager = LocalFocusManager.current,
    focusRequester: FocusRequester = FocusRequester()
) = remember {
    MaxWidthTextFieldStateHolder(
        _textFieldState = textField,
        label = label,
        placeholder = placeholder,
        showKeyboardEnabled = showKeyboardEnabled,
        keyboardOptions = keyboardOptions,
        focusManager = focusManager,
        focusRequester = focusRequester
    )
}

@Composable
fun rememberEmailTextFieldState(
    showKeyboardEnabled: Boolean = false,
    imeAction: ImeAction = ImeAction.Done,
) = rememberMaxWidthTextFiledState(
    label = R.string.email,
    placeholder = R.string.email_place_holder,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = imeAction),
    showKeyboardEnabled = showKeyboardEnabled,
)

@Composable
fun rememberPasswordTextFieldState(
    @StringRes label: Int = R.string.password,
    imeAction: ImeAction = ImeAction.Done
) = rememberMaxWidthTextFiledState(
    label = label,
    keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = imeAction
    )
)