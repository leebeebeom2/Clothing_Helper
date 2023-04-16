@file:OptIn(ExperimentalComposeUiApi::class)

package com.leebeebeom.clothinghelper.ui.component

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.theme.DarkGrayishBlue
import com.leebeebeom.clothinghelper.ui.theme.Gainsboro
import com.leebeebeom.clothinghelper.ui.theme.LightGrayishBlue
import com.leebeebeom.clothinghelper.ui.theme.VeryDarkGray
import com.leebeebeom.clothinghelper.ui.util.Anime
import com.leebeebeom.clothinghelper.ui.util.Anime.Error.errorIn
import com.leebeebeom.clothinghelper.ui.util.Anime.Error.errorOut
import kotlinx.coroutines.delay

@NoLiveLiterals
@Composable // skippable
fun StatefulMaxWidthTextFieldWithCancelIcon(
    @StringRes label: Int? = null,
    @StringRes placeholder: Int? = null,
    error: () -> Int? = { null },
    isVisible: () -> Boolean = { true },
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
    ),
    onInputChange: (String) -> Unit,
    getFocus: Boolean = false,
    fixedError: Boolean = false,
    state: MaxWidthTextFieldState = rememberMaxWidthTestFieldState()
) {
    MaxWidthTextFieldWithError(
        textFieldValue = { state.textFieldValue },
        onValueChange = state::onValueChange,
        onFocusChanged = state::onFocusChanged,
        label = label,
        placeholder = placeholder,
        error = error,
        isVisible = isVisible,
        keyboardOptions = keyboardOptions,
        trailingIcon = {
            TextFieldCancelIcon(hasFocus = { state.hasFocus },
                onValueChange = state::onValueChange,
                textFieldValue = { state.textFieldValue })
        },
        onInputChange = onInputChange,
        showKeyboard = getFocus,
        fixedError = fixedError
    )
}

@NoLiveLiterals
@Composable // skippable
fun StatefulMaxWidthTextField(
    @StringRes label: Int? = null,
    @StringRes placeholder: Int? = null,
    error: () -> Int? = { null },
    isVisible: () -> Boolean = { true },
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
    ),
    onInputChange: (String) -> Unit = {},
    trailingIcon: @Composable (() -> Unit)? = null,
    getFocus: Boolean = false,
    state: MaxWidthTextFieldState = rememberMaxWidthTestFieldState()
) {
    MaxWidthTextFieldWithError(
        textFieldValue = { state.textFieldValue },
        onValueChange = state::onValueChange,
        onFocusChanged = state::onFocusChanged,
        label = label,
        placeholder = placeholder,
        error = error,
        isVisible = isVisible,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        onInputChange = onInputChange,
        showKeyboard = getFocus,
        fixedError = false
    )
}

@Composable // skippable
fun MaxWidthTextFieldWithError(
    textFieldValue: () -> TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    @StringRes label: Int?,
    @StringRes placeholder: Int? = null,
    error: () -> Int? = { null },
    isVisible: () -> Boolean = { true },
    keyboardOptions: KeyboardOptions,
    trailingIcon: @Composable (() -> Unit)? = null,
    onInputChange: (String) -> Unit,
    showKeyboard: Boolean,
    fixedError: Boolean,
) {
    val focusRequester = remember { FocusRequester() }

    Column {
        MaxWidthTextField(
            label = label,
            placeholder = placeholder,
            textFieldValue = textFieldValue,
            error = error,
            isVisible = isVisible,
            keyboardOptions = keyboardOptions,
            trailingIcon = trailingIcon,
            onValueChange = onValueChange,
            onFocusChanged = onFocusChanged,
            focusRequester = focusRequester
        )
        ErrorText(error = error, fixedError = fixedError)
        ShowKeyboard(focusRequester = focusRequester, showKeyboard = showKeyboard)
        TextFieldEmit(textFieldValue = textFieldValue, onInputChange = onInputChange)
    }
}

@Composable // skippable
private fun MaxWidthTextField(
    @StringRes label: Int?,
    @StringRes placeholder: Int?,
    textFieldValue: () -> TextFieldValue,
    error: () -> Int?,
    isVisible: () -> Boolean,
    keyboardOptions: KeyboardOptions,
    trailingIcon: (@Composable () -> Unit)? = null,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    focusRequester: FocusRequester
) {
    val focusManager = LocalFocusManager.current
    val localTextFiledValue by remember(textFieldValue) { derivedStateOf(textFieldValue) }
    val localError by remember(error) { derivedStateOf(error) }
    val localIsVisible by remember(isVisible) { derivedStateOf(isVisible) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged(onFocusChanged = onFocusChanged),
        label = label?.let { { SingleLineText(text = it) } },
        placeholder = placeholder?.let { { SingleLineText(text = it) } },
        value = localTextFiledValue,
        onValueChange = onValueChange,
        isError = localError != null,
        visualTransformation = if (localIsVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        keyboardActions = if (keyboardOptions.imeAction == ImeAction.Done) KeyboardActions(onDone = { focusManager.clearFocus() }) else KeyboardActions.Default,
        trailingIcon = { if (trailingIcon != null) trailingIcon() },
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Gainsboro,
            unfocusedLabelColor = DarkGrayishBlue,
            backgroundColor = LightGrayishBlue,
            placeholderColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)
        )
    )
}

@Composable // skippable
private fun ErrorText(error: () -> Int?, fixedError: Boolean) {
    val localError by remember(error) { derivedStateOf(error) }
    val show by remember(error) { derivedStateOf { localError != null } }

    @Composable
    fun animatedErrorText() {
        AnimatedVisibility(
            visible = show, enter = errorIn, exit = errorOut
        ) {
            SingleLineText(
                modifier = Modifier.padding(start = 4.dp, top = 4.dp),
                text = localError,
                style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.error)
            )
        }
    }

    if (fixedError)
        Box(modifier = Modifier.fillMaxWidth()) {
            SingleLineText(
                modifier = Modifier.padding(vertical = 8.dp),
                text = "",
                style = MaterialTheme.typography.caption
            )
            animatedErrorText()
        }
    else animatedErrorText()
}

@Composable // skippable
private fun ShowKeyboard(
    showKeyboard: Boolean,
    focusRequester: FocusRequester
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var showedKeyboard by rememberSaveable { mutableStateOf(false) }

    if (showKeyboard && !showedKeyboard) {
        LaunchedEffect(key1 = Unit) {
            focusRequester.requestFocus()
            delay(100)
            keyboardController?.show()
            showedKeyboard = true
        }
    }

}

@Composable // skippable
private fun TextFieldEmit(textFieldValue: () -> TextFieldValue, onInputChange: (String) -> Unit) {
    val localTextFiledValue by remember(textFieldValue) { derivedStateOf(textFieldValue) }
    val currentOnInputChange by rememberUpdatedState(newValue = onInputChange)

    LaunchedEffect(key1 = textFieldValue) {
        snapshotFlow { localTextFiledValue.text.trim() }.collect(currentOnInputChange)
    }
}

const val CancelIconTag = "cancel Icon"

@Composable // skippable
fun TextFieldCancelIcon(
    hasFocus: () -> Boolean,
    onValueChange: (TextFieldValue) -> Unit,
    textFieldValue: () -> TextFieldValue,
) {
    val localHasFocus by remember(hasFocus) { derivedStateOf(hasFocus) }
    val localTextFieldValue by remember(textFieldValue) { derivedStateOf(textFieldValue) }

    AnimatedVisibility(
        visible = localHasFocus && localTextFieldValue.text.isNotBlank(),
        enter = Anime.CancelIcon.fadeIn,
        exit = Anime.CancelIcon.fadeOut,
    ) {
        CustomIconButton(
            modifier = Modifier.testTag(CancelIconTag),
            onClick = { onValueChange(TextFieldValue()) },
            drawable = R.drawable.ic_cancel,
            tint = VeryDarkGray,
            size = 20.dp
        )
    }
}

@Composable
fun rememberMaxWidthTestFieldState(
    initialText: String = "", blockBlank: Boolean = false
) = rememberSaveable(saver = MaxWidthTextFieldState.Saver) {
    MaxWidthTextFieldState(
        initialTextFieldValue = TextFieldValue(initialText), blockBlank = blockBlank
    )
}

// stable
open class MaxWidthTextFieldState(
    initialTextFieldValue: TextFieldValue, private val blockBlank: Boolean
) {
    var textFieldValue by mutableStateOf(initialTextFieldValue)
        protected set
    var hasFocus by mutableStateOf(false)
        protected set

    fun onValueChange(value: TextFieldValue) {
        textFieldValue = if (blockBlank) value.copy(value.text.trim()) else value
    }

    open fun onFocusChanged(focusState: FocusState) {
        hasFocus = focusState.hasFocus
        if (focusState.hasFocus) {
            textFieldValue = textFieldValue.copy(
                text = textFieldValue.text, selection = TextRange(textFieldValue.text.length)
            )
        }
    }

    companion object {
        val Saver = listSaver<MaxWidthTextFieldState, Any>(save = {
            listOf(
                it.textFieldValue.text,
                it.textFieldValue.selection.start,
                it.textFieldValue.selection.end,
                it.blockBlank
            )
        }, restore = {
            MaxWidthTextFieldState(
                initialTextFieldValue = TextFieldValue(
                    it[0] as String, TextRange(it[1] as Int, it[2] as Int)
                ), blockBlank = it[3] as Boolean
            )
        })
    }
}