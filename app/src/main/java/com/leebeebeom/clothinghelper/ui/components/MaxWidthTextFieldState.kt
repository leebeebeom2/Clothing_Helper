package com.leebeebeom.clothinghelper.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MaxWidthTextField(
    modifier: Modifier = Modifier,
    state: MaxWidthTextFieldState
) {
    Column(modifier = modifier) {
        MaxWidthTextField(state = state)
        ErrorText(state = state)
    }
    ShowKeyboard(state = state)
}

@Composable
private fun MaxWidthTextField(state: MaxWidthTextFieldState) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(state.focusRequester)
            .onFocusChanged(onFocusChanged = state::onFocusChanged),
        value = state.textFieldValue,
        onValueChange = state::onValueChange,
        label = { SingleLineText(text = state.label) },
        placeholder = state.placeholder?.let { { SingleLineText(text = state.placeholder) } },
        isError = state.isError,
        visualTransformation = if (state.isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = state.keyboardOptions,
        trailingIcon = { state.TrailingIcon() },
        singleLine = true,
        keyboardActions = state.keyboardActions,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Gainsboro,
            unfocusedLabelColor = DarkGrayishBlue,
            backgroundColor = LightGrayishBlue,
            placeholderColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)
        ),
        enabled = state.enabled
    )

    TextFieldDefaults.textFieldColors()
}

@Composable
private fun ErrorText(state: MaxWidthTextFieldState) {
    AnimatedVisibility(
        visible = state.isError, enter = errorIn, exit = errorOut
    ) {
        SingleLineText(
            modifier = Modifier.padding(start = 4.dp, top = 4.dp),
            text = state.error,
            style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.error)
        )
    }
}

@Stable
@OptIn(ExperimentalComposeUiApi::class)
interface MaxWidthTextFieldState {
    // mutable
    val textFieldValue: TextFieldValue
    val error: Int?
    val isError get() = error != null
    val hasFocus: Boolean
    val enabled: Boolean
    val isVisible: Boolean

    // immutable
    @get:StringRes
    val label: Int

    @get:StringRes
    val placeholder: Int?

    val showKeyboard: Boolean
    val keyboardOptions: KeyboardOptions
    val cancelIconEnabled: Boolean

    val focusRequester: FocusRequester
    val keyboardController: SoftwareKeyboardController?
    val focusManager: FocusManager
    val trailingIcon: @Composable (() -> Unit)?
    val keyboardActions: KeyboardActions
    fun onValueChange(newTextField: TextFieldValue)
    fun showKeyboard(scope: CoroutineScope)
    fun onFocusChanged(focusState: FocusState)
    fun clearFocus() {
        focusManager.clearFocus()
    }

    @Composable
    fun TrailingIcon() {
        if (trailingIcon != null || cancelIconEnabled)
            Row {
                trailingIcon?.run { invoke() } ?: CancelIcon()
                WidthSpacer(dp = 4)
            }
    }

    @Composable
    private fun CancelIcon() {
        AnimatedVisibility(
            visible = textFieldValue.text.isNotEmpty() && hasFocus,
            enter = Anime.CancelIcon.fadeIn,
            exit = Anime.CancelIcon.fadeOut,
        ) {
            CustomIconButton(
                onClick = { onValueChange(TextFieldValue()) },
                drawable = R.drawable.ic_cancel,
                tint = VeryDarkGray,
                size = 20.dp
            )
        }
    }
}

@Stable
@OptIn(ExperimentalComposeUiApi::class)
open class MutableMaxWidthTextFieldState(
    initialText: String,
    initialVisibility: Boolean,
    override val label: Int,
    override val placeholder: Int?,
    override val showKeyboard: Boolean,
    final override val keyboardOptions: KeyboardOptions,
    override val cancelIconEnabled: Boolean,
    override val keyboardController: SoftwareKeyboardController?,
    override val focusManager: FocusManager,
    override val trailingIcon: @Composable (() -> Unit)?
) : MaxWidthTextFieldState {
    override var textFieldValue by mutableStateOf(TextFieldValue(initialText))
    override var error: Int? by mutableStateOf(null)
    override val hasFocus by mutableStateOf(false)
    override val enabled by mutableStateOf(true)
    override val isVisible by mutableStateOf(initialVisibility)
    override val keyboardActions =
        if (keyboardOptions.imeAction == ImeAction.Done)
            KeyboardActions(onDone = { clearFocus() })
        else KeyboardActions.Default
    override val focusRequester: FocusRequester = FocusRequester()

    override fun onValueChange(newTextField: TextFieldValue) {
        textFieldValue = newTextField
    }

    override fun showKeyboard(scope: CoroutineScope) {
        scope.launch {
            focusRequester.requestFocus()
            delay(100)
            keyboardController?.show()
        }
    }

    override fun onFocusChanged(focusState: FocusState) {}
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@NonRestartableComposable
fun rememberMaxWidthTextFieldState(
    initialText: String,
    initialVisibility: Boolean = true,
    @StringRes label: Int,
    @StringRes placeholder: Int? = null,
    showKeyboard: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    cancelIconEnabled: Boolean = true,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    focusManager: FocusManager = LocalFocusManager.current,
    trailingIcon: @Composable (() -> Unit)? = null
): MutableMaxWidthTextFieldState = remember {
    MutableMaxWidthTextFieldState(
        initialText = initialText,
        initialVisibility = initialVisibility,
        label = label,
        placeholder = placeholder,
        showKeyboard = showKeyboard,
        keyboardOptions = keyboardOptions,
        cancelIconEnabled = cancelIconEnabled,
        keyboardController = keyboardController,
        focusManager = focusManager,
        trailingIcon = trailingIcon
    )
}

@Composable
private fun ShowKeyboard(state: MaxWidthTextFieldState) {
    if (state.showKeyboard) LaunchedEffect(key1 = Unit, state::showKeyboard)
}