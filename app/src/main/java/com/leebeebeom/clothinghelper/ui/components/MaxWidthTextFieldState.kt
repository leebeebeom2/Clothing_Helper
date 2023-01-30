@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class)

package com.leebeebeom.clothinghelper.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class KeyboardRoute {
    TEXT, EMAIL, PASSWORD
}

enum class ImeActionRoute {
    NEXT, DONE
}

@Composable
fun MaxWidthTextFieldWithError(
    modifier: Modifier = Modifier,
    state: MaxWidthTextFieldState,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    onInputChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        MaxWidthTextField(
            state = state,
            trailingIcon = trailingIcon,
            onValueChange = onValueChange,
            onFocusChanged = onFocusChanged
        )
        ErrorText(state = state)
    }
    ShowKeyboard(state = state)
    TextFieldEmit(state = state, onInputChange = onInputChange)
}

@Composable
private fun MaxWidthTextField(
    state: MaxWidthTextFieldState,
    trailingIcon: @Composable (() -> Unit)?,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    focusManager: FocusManager = LocalFocusManager.current
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(state.focusRequester)
            .onFocusChanged(onFocusChanged = onFocusChanged),
        value = state.textFieldValue,
        onValueChange = onValueChange,
        label = { SingleLineText(text = state.label) },
        placeholder = state.placeholder?.let { { SingleLineText(text = state.placeholder) } },
        isError = state.isError,
        visualTransformation = if (state.isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = state.keyboardOptions,
        trailingIcon = {
            state.TrailingIcon(
                trailingIcon = trailingIcon, onValueChange = onValueChange
            )
        },
        singleLine = true,
        keyboardActions = state.getKeyboardActions(focusManager = focusManager),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Gainsboro,
            unfocusedLabelColor = DarkGrayishBlue,
            backgroundColor = LightGrayishBlue,
            placeholderColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)
        ),
        enabled = state.enabled
    )
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
    val isError: Boolean
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
    val keyboardRoute: KeyboardRoute
    val imeActionRoute: ImeActionRoute

    fun showKeyboard(scope: CoroutineScope, keyboardController: SoftwareKeyboardController?)
    fun getKeyboardActions(focusManager: FocusManager): KeyboardActions

    @Composable
    fun TrailingIcon(
        trailingIcon: @Composable (() -> Unit)?, onValueChange: (TextFieldValue) -> Unit
    ) {
        if (trailingIcon != null || cancelIconEnabled) Row {
            trailingIcon?.run { invoke() } ?: CancelIcon(onValueChange = onValueChange)
            WidthSpacer(dp = 4)
        }
    }

    @Composable
    private fun CancelIcon(onValueChange: (TextFieldValue) -> Unit) {
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

open class MutableMaxWidthTextFieldState(
    initialText: String,
    initialError: Int?,
    initialHasFocus: Boolean = false,
    initialEnabled: Boolean,
    initialVisibility: Boolean,
    final override val keyboardRoute: KeyboardRoute,
    final override val imeActionRoute: ImeActionRoute,
    override val label: Int,
    override val placeholder: Int?,
    override val showKeyboard: Boolean,
    override val cancelIconEnabled: Boolean,
    private val blockBlank: Boolean
) : MaxWidthTextFieldState {
    override var textFieldValue by mutableStateOf(TextFieldValue(initialText))
    override var error: Int? by mutableStateOf(initialError)
    override val isError by derivedStateOf { error != null }
    override var hasFocus by mutableStateOf(initialHasFocus)
    override var enabled by mutableStateOf(initialEnabled)
    override var isVisible by mutableStateOf(initialVisibility)

    private val keyboardType = when (keyboardRoute) {
        KeyboardRoute.TEXT -> KeyboardType.Text
        KeyboardRoute.EMAIL -> KeyboardType.Email
        KeyboardRoute.PASSWORD -> KeyboardType.Password
    }
    private val imeAction = when (imeActionRoute) {
        ImeActionRoute.NEXT -> ImeAction.Next
        ImeActionRoute.DONE -> ImeAction.Done
    }

    override val keyboardOptions =
        KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction)

    override val focusRequester: FocusRequester = FocusRequester()

    open fun onValueChange(newTextField: TextFieldValue) {
        if (blockBlank) {
            textFieldValue = newTextField.copy(text = newTextField.text.trim())
            if (textFieldValue.text != newTextField.text) error = null
        } else {
            textFieldValue = newTextField
            error = null
        }
    }

    override fun showKeyboard(
        scope: CoroutineScope, keyboardController: SoftwareKeyboardController?
    ) {
        scope.launch {
            focusRequester.requestFocus()
            delay(100)
            keyboardController?.show()
        }
    }

    fun onFocusChanged(focusState: FocusState) {
        hasFocus = focusState.hasFocus
        if (hasFocus) textFieldValue =
            textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
    }

    override fun getKeyboardActions(focusManager: FocusManager) =
        if (imeAction == ImeAction.Done) KeyboardActions(onDone = { focusManager.clearFocus() })
        else KeyboardActions.Default

    fun visibilityToggle() {
        isVisible = !isVisible
    }

    companion object {
        val Saver: Saver<MutableMaxWidthTextFieldState, *> = listSaver(save = {
            listOf(
                it.textFieldValue.text,
                it.error,
                it.hasFocus,
                it.enabled,
                it.isVisible,
                it.keyboardRoute,
                it.imeActionRoute,
                it.label,
                it.placeholder,
                it.showKeyboard,
                it.cancelIconEnabled,
                it.blockBlank
            )
        }, restore = {
            MutableMaxWidthTextFieldState(
                initialText = it[0] as String,
                initialError = it[1] as Int?,
                initialHasFocus = it[2] as Boolean,
                initialEnabled = it[3] as Boolean,
                initialVisibility = it[4] as Boolean,
                keyboardRoute = it[5] as KeyboardRoute,
                imeActionRoute = it[6] as ImeActionRoute,
                label = it[7] as Int,
                placeholder = it[8] as Int?,
                showKeyboard = it[9] as Boolean,
                cancelIconEnabled = it[10] as Boolean,
                blockBlank = it[11] as Boolean
            )
        })
    }
}

@Composable
@NonRestartableComposable
fun rememberMaxWidthTextFieldState(
    initialInput: String = "",
    initialError: () -> Int? = { null },
    initialEnabled: Boolean = true,
    initialVisibility: Boolean = true,
    keyboardRoute: KeyboardRoute = KeyboardRoute.TEXT,
    imeActionRoute: ImeActionRoute = ImeActionRoute.DONE,
    @StringRes label: Int,
    @StringRes placeholder: Int? = null,
    showKeyboard: Boolean = false,
    cancelIconEnabled: Boolean = true,
    blockBlank: Boolean = false
): MutableMaxWidthTextFieldState = rememberSaveable(
    saver = MutableMaxWidthTextFieldState.Saver, inputs = arrayOf(
        initialInput,
        initialError,
        initialEnabled,
        initialVisibility,
        keyboardRoute,
        imeActionRoute,
        label,
        placeholder,
        showKeyboard,
        cancelIconEnabled,
        blockBlank
    )
) {
    MutableMaxWidthTextFieldState(
        initialText = initialInput,
        initialError = initialError(),
        initialEnabled = initialEnabled,
        initialVisibility = initialVisibility,
        keyboardRoute = keyboardRoute,
        imeActionRoute = imeActionRoute,
        label = label,
        placeholder = placeholder,
        showKeyboard = showKeyboard,
        cancelIconEnabled = cancelIconEnabled,
        blockBlank = blockBlank
    )
}

@Composable
private fun ShowKeyboard(
    state: MaxWidthTextFieldState,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
) {
    if (state.showKeyboard) LaunchedEffect(
        key1 = Unit
    ) { state.showKeyboard(scope = this, keyboardController = keyboardController) }
}

@Composable
private fun TextFieldEmit(state: MaxWidthTextFieldState, onInputChange: (String) -> Unit) {
    val currentOnInputChange by rememberUpdatedState(newValue = onInputChange)
    LaunchedEffect(key1 = state) {
        snapshotFlow { state.textFieldValue.text.trim() }.collect(currentOnInputChange)
    }
}