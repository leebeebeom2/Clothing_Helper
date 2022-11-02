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
    elementsState: MaxWidthTextFieldElementsState,
    state: MaxWidthTextFieldUIState,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester = elementsState.focusRequester)
                .onFocusChanged(onFocusChanged = state::onFocusChanged),
            value = state.textFiled,
            onValueChange = state::onValueChange,
            label = { Text(text = stringResource(id = elementsState.label)) },
            placeholder = { Text(text = stringResource(id = elementsState.placeholder)) },
            isError = state.isError,
            visualTransformation = visualTransformation,
            singleLine = true,
            maxLines = 1,
            keyboardOptions = elementsState.keyboardOptions,
            trailingIcon = trailingIcon,
            keyboardActions =
            if (elementsState.keyboardOptions.imeAction == ImeAction.Done)
                KeyboardActions(onDone = { elementsState.onKeyBoardActionDoneClick() })
            else KeyboardActions.Default,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFDADADA),
                unfocusedLabelColor = Color(0xFF8391A1),
                backgroundColor = Color(0xFFF7F8F9),
                placeholderColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)
            )
        )

        ErrorText(state.error)
    }
    if (elementsState.showKeyboardEnabled) ShowKeyboard(elementsState.focusRequester)
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

data class MaxWidthTextFieldElementsState(
    @StringRes val label: Int,
    @StringRes val placeholder: Int,
    val showKeyboardEnabled: Boolean,
    val keyboardOptions: KeyboardOptions,
    val focusManager: FocusManager,
    val focusRequester: FocusRequester
) {
    fun onKeyBoardActionDoneClick() {
        focusManager.clearFocus()
    }
}

@Composable
fun rememberMaxWidthTextFiledElementsState(
    @StringRes label: Int,
    @StringRes placeholder: Int = R.string.empty,
    showKeyboardEnabled: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done
    ),
    focusManager: FocusManager = LocalFocusManager.current,
    focusRequester: FocusRequester = FocusRequester()
) = remember {
    MaxWidthTextFieldElementsState(
        label = label,
        placeholder = placeholder,
        showKeyboardEnabled = showKeyboardEnabled,
        keyboardOptions = keyboardOptions,
        focusManager = focusManager,
        focusRequester = focusRequester
    )
}

@Composable
fun rememberEmailTextFieldElementsState(
    showKeyboardEnabled: Boolean = false,
    imeAction: ImeAction = ImeAction.Done,
) = rememberMaxWidthTextFiledElementsState(
    label = R.string.email,
    placeholder = R.string.email_place_holder,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = imeAction),
    showKeyboardEnabled = showKeyboardEnabled,
)

@Composable
fun rememberNameTextFieldElementsState() = rememberMaxWidthTextFiledElementsState(label = R.string.name)

@Composable
fun rememberPasswordTextFieldElementsState(@StringRes label: Int = R.string.password) =
    rememberMaxWidthTextFiledElementsState(
        label = R.string.password,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        )
    )

@Composable
fun rememberPasswordConfirmTextFieldElementsState() =
    rememberPasswordTextFieldElementsState(label = R.string.password_confirm)

open class MaxWidthTextFieldUIState(
    text: String = "",
    error: Int? = null
) {
    var textFiled by mutableStateOf(TextFieldValue(text))
        protected set
    var error: Int? by mutableStateOf(error)
        protected set

    val isError get() = error != null

    open fun onValueChange(newText: TextFieldValue) {
        if (textFiled.text != newText.text) updateError(null)
        textFiled = newText
    }

    open fun updateError(@StringRes error: Int?) {
        this.error = error
    }

    open fun onFocusChanged(focusState: FocusState) {
        if (focusState.hasFocus)
            textFiled = textFiled.copy(selection = TextRange(textFiled.text.length))
    }
}