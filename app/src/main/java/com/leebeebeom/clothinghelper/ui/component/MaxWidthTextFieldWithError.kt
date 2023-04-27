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
import com.leebeebeom.clothinghelper.ui.util.OnFocusChanged
import com.leebeebeom.clothinghelper.ui.util.OnValueChange
import kotlinx.coroutines.delay

@NoLiveLiterals
@Composable
fun MaxWidthTextFieldWithErrorAndCancelIcon(
    state: MaxWidthTextFieldState,
    @StringRes label: Int,
    @StringRes placeholder: Int? = null,
    error: () -> Int? = { null },
    isVisible: () -> Boolean = { true },
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
    ),
    onInputChange: (String) -> Unit,
    getFocus: Boolean = false,
    fixedError: Boolean = false,
    onValueChange: OnValueChange,
    onFocusChanged: OnFocusChanged
) {
    MaxWidthTextFieldWithError(
        state = state,
        onValueChange = onValueChange,
        onFocusChanged = onFocusChanged,
        label = label,
        placeholder = placeholder,
        error = error,
        isVisible = isVisible,
        keyboardOptions = keyboardOptions,
        trailingIcon = {
            TextFieldCancelIcon(hasFocus = { state.hasFocus },
                onValueChange = onValueChange,
                textFieldValue = { state.textFieldValue })
        },
        onInputChange = onInputChange,
        showKeyboard = getFocus,
        fixedError = fixedError
    )
}

@NoLiveLiterals
@Composable
fun MaxWidthTextFieldWithError(
    state: MaxWidthTextFieldState,
    @StringRes label: Int,
    @StringRes placeholder: Int? = null,
    error: () -> Int? = { null },
    isVisible: () -> Boolean = { true },
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
    ),
    onInputChange: (String) -> Unit = {},
    trailingIcon: @Composable (() -> Unit)? = null,
    getFocus: Boolean = false,
    focusRequester: FocusRequester,
    onValueChange: OnValueChange,
    onFocusChanged: OnFocusChanged
) {
    MaxWidthTextFieldWithError(
        state = state,
        onValueChange = onValueChange,
        onFocusChanged = onFocusChanged,
        label = label,
        placeholder = placeholder,
        error = error,
        isVisible = isVisible,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        onInputChange = onInputChange,
        showKeyboard = getFocus,
        fixedError = false,
        focusRequester = focusRequester
    )
}

@Composable
fun MaxWidthTextFieldWithError(
    state: MaxWidthTextFieldState,
    onValueChange: OnValueChange,
    onFocusChanged: OnFocusChanged,
    @StringRes label: Int,
    @StringRes placeholder: Int?,
    error: () -> Int?,
    isVisible: () -> Boolean,
    keyboardOptions: KeyboardOptions,
    trailingIcon: @Composable (() -> Unit)?,
    onInputChange: (String) -> Unit,
    showKeyboard: Boolean,
    fixedError: Boolean,
    focusRequester: FocusRequester = remember { FocusRequester() }
) {
    Column {
        MaxWidthTextField(
            label = label,
            placeholder = placeholder,
            textFieldValue = { state.textFieldValue },
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
        TextFieldEmit(textFieldValue = { state.textFieldValue }, onInputChange = onInputChange)
    }
}

@Composable
private fun MaxWidthTextField(
    @StringRes label: Int,
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

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged(onFocusChanged = onFocusChanged),
        label = { SingleLineText(text = label) },
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

@Composable
private fun ErrorText(error: () -> Int?, fixedError: Boolean) {
    val show by remember(error) { derivedStateOf { error() != null } }

    @Composable
    fun animatedErrorText() {
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
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

@Composable
private fun TextFieldEmit(textFieldValue: () -> TextFieldValue, onInputChange: (String) -> Unit) {
    val currentOnInputChange by rememberUpdatedState(newValue = onInputChange)

    LaunchedEffect(key1 = textFieldValue) {
        snapshotFlow { textFieldValue().text.trim() }.collect(currentOnInputChange)
    }
}

const val CancelIconTag = "cancel Icon"

@Composable
fun TextFieldCancelIcon(
    hasFocus: () -> Boolean,
    onValueChange: OnValueChange,
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
) = rememberSaveable(saver = MutableMaxWidthTextFieldState.Saver) {
    MutableMaxWidthTextFieldState(
        initialTextFieldValue = TextFieldValue(initialText), blockBlank = blockBlank
    )
}

@Stable
interface MaxWidthTextFieldState {
    val textFieldValue: TextFieldValue
    val hasFocus: Boolean
}

open class MutableMaxWidthTextFieldState(
    initialTextFieldValue: TextFieldValue,
    private val blockBlank: Boolean
) : MaxWidthTextFieldState {
    override var textFieldValue by mutableStateOf(initialTextFieldValue)
        protected set
    override var hasFocus by mutableStateOf(false)
        protected set

    fun onValueChange(value: TextFieldValue) {
        textFieldValue = if (blockBlank) value.copy(value.text.trim()) else value
    }

    open fun onFocusChanged(focusState: FocusState) {
        hasFocus = focusState.hasFocus

        if (!focusState.hasFocus) return

        textFieldValue = textFieldValue.copy(
            text = textFieldValue.text, selection = TextRange(textFieldValue.text.length)
        )
    }

    companion object {
        val Saver = listSaver<MutableMaxWidthTextFieldState, Any>(save = {
            listOf(
                it.textFieldValue.text,
                it.textFieldValue.selection.start,
                it.textFieldValue.selection.end,
                it.blockBlank
            )
        }, restore = {
            MutableMaxWidthTextFieldState(
                initialTextFieldValue = TextFieldValue(
                    it[0] as String, TextRange(it[1] as Int, it[2] as Int)
                ), blockBlank = it[3] as Boolean
            )
        })
    }
}