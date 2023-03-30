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
import androidx.compose.ui.platform.SoftwareKeyboardController
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
fun StatefulMaxWidthTestFieldWithCancelIcon(
    initialText: String = "",
    blockBlank: Boolean = false,
    @StringRes label: Int? = null,
    @StringRes placeholder: Int? = null,
    error: () -> Int? = { null },
    isVisible: () -> Boolean = { true },
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
    ),
    onInputChange: (String) -> Unit,
    focusRequester: FocusRequester = remember { FocusRequester() },
    getFocus: Boolean = false,
    state: MaxWidthTextFieldState = rememberMaxWidthTestFieldState(
        initialText = initialText, blockBlank = blockBlank
    )
) {
    MaxWidthTextFieldWithError(
        textFieldValue = { state.textFieldValue },
        onValueChange = state::onValueChange,
        onFocusChanged = state::onFocusChanged,
        keyboardOptions = keyboardOptions,
        onInputChange = onInputChange,
        focusRequester = focusRequester,
        trailingIcon = {
            TextFieldCancelIcon(hasFocus = { state.hasFocus },
                onValueChange = state::onValueChange,
                textFieldValue = { state.textFieldValue })
        },
        label = label,
        placeholder = placeholder,
        error = error,
        isVisible = isVisible,
        getFocus = getFocus
    )
}

@NoLiveLiterals
@Composable // skippable
fun StatefulMaxWidthTextField(
    initialText: String = "",
    blockBlank: Boolean = false,
    @StringRes label: Int? = null,
    @StringRes placeholder: Int? = null,
    error: () -> Int? = { null },
    isVisible: () -> Boolean = { true },
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
    ),
    onInputChange: (String) -> Unit = {},
    focusRequester: FocusRequester = remember { FocusRequester() },
    trailingIcon: @Composable (() -> Unit)? = null,
    getFocus: Boolean = false,
    state: MaxWidthTextFieldState = rememberMaxWidthTestFieldState(
        initialText = initialText, blockBlank = blockBlank
    )
) {
    MaxWidthTextFieldWithError(
        textFieldValue = { state.textFieldValue },
        onValueChange = state::onValueChange,
        onFocusChanged = state::onFocusChanged,
        keyboardOptions = keyboardOptions,
        onInputChange = onInputChange,
        focusRequester = focusRequester,
        trailingIcon = trailingIcon,
        label = label,
        placeholder = placeholder,
        error = error,
        isVisible = isVisible,
        getFocus = getFocus
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
    trailingIcon: (@Composable () -> Unit)? = null,
    onInputChange: (String) -> Unit,
    focusRequester: FocusRequester = remember { FocusRequester() },
    getFocus: Boolean
) {
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
        ErrorText(error = error)
    }
    ShowKeyboard(focusRequester = focusRequester, getFocus = getFocus)
    TextFieldEmit(textFieldValue = textFieldValue, onInputChange = onInputChange)
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
    focusManager: FocusManager = LocalFocusManager.current,
    focusRequester: FocusRequester
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged(onFocusChanged = onFocusChanged),
        label = label?.let { { SingleLineText(text = it) } },
        placeholder = placeholder?.let { { SingleLineText(text = it) } },
        value = textFieldValue(),
        onValueChange = onValueChange,
        isError = error() != null,
        visualTransformation = if (isVisible()) VisualTransformation.None else PasswordVisualTransformation(),
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
private fun ErrorText(error: () -> Int?) {
    val show by remember { derivedStateOf { error() != null } }

    AnimatedVisibility(
        visible = show, enter = errorIn, exit = errorOut
    ) {
        SingleLineText(
            modifier = Modifier.padding(start = 4.dp, top = 4.dp),
            text = error(),
            style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.error)
        )
    }
}

@Composable // skippable
private fun ShowKeyboard(
    getFocus: Boolean,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    focusRequester: FocusRequester
) {
    var showedKeyboard by rememberSaveable { mutableStateOf(false) }
    if (getFocus && !showedKeyboard) {
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
    val currentOnInputChange by rememberUpdatedState(newValue = onInputChange)

    LaunchedEffect(key1 = textFieldValue) {
        snapshotFlow { textFieldValue().text.trim() }.collect(currentOnInputChange)
    }
}

const val CancelIconTag = "cancel Icon"

@Composable // skippable
fun TextFieldCancelIcon(
    hasFocus: () -> Boolean,
    onValueChange: (TextFieldValue) -> Unit,
    textFieldValue: () -> TextFieldValue,
) {
    AnimatedVisibility(
        visible = hasFocus() && textFieldValue().text.isNotBlank(),
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