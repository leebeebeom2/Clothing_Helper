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
    maxWidthTextFieldState: MaxWidthTextFieldState,
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
                .focusRequester(focusRequester = maxWidthTextFieldState.focusRequester)
                .onFocusChanged(onFocusChanged = onFocusChanged),
            value = maxWidthTextFieldState.textField.value,
            onValueChange = onValueChange,
            label = { Text(text = stringResource(id = maxWidthTextFieldState.label)) },
            placeholder = { Text(text = stringResource(id = maxWidthTextFieldState.placeholder)) },
            isError = error != null,
            visualTransformation = visualTransformation,
            singleLine = true,
            maxLines = 1,
            keyboardOptions = maxWidthTextFieldState.keyboardOptions,
            trailingIcon = trailingIcon,
            keyboardActions =
            if (maxWidthTextFieldState.keyboardOptions.imeAction == ImeAction.Done)
                KeyboardActions(onDone = { maxWidthTextFieldState.onKeyBoardActionDoneClick() })
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
    if (maxWidthTextFieldState.showKeyboardEnabled) ShowKeyboard(maxWidthTextFieldState.focusRequester)
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

data class MaxWidthTextFieldState(
    val textField: MutableState<TextFieldValue>,
    @StringRes val label: Int,
    @StringRes val placeholder: Int,
    val showKeyboardEnabled: Boolean,
    val keyboardOptions: KeyboardOptions,
    val focusManager: FocusManager,
    val focusRequester: FocusRequester
) {
    val text get() = textField.value.text

    fun onKeyBoardActionDoneClick() {
        focusManager.clearFocus()
    }

    open fun onValueChange(newText: TextFieldValue, updateError: (Int?) -> Unit) {
        if (text != newText.text) updateError(null)
        textField.value = newText
    }

    open fun onFocusChanged(focusState: FocusState) {
        if (focusState.hasFocus)
            textField.value = textField.value.copy(selection = TextRange(text.length))
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
    MaxWidthTextFieldState(
        textField = textField,
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
fun rememberNameTextFieldState() = rememberMaxWidthTextFiledState(label = R.string.name)

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

@Composable
fun rememberPasswordConfirmTextFieldState() =
    rememberPasswordTextFieldState(label = R.string.password_confirm)