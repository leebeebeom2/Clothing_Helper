@file:OptIn(
    ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class,
    ExperimentalComposeUiApi::class
)

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
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.components.ImeActionRoute.DONE
import com.leebeebeom.clothinghelper.ui.components.ImeActionRoute.NEXT
import com.leebeebeom.clothinghelper.ui.components.KeyboardRoute.*
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
    state: MaxWidthTextFieldState,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    onInputChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column {
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
        label = state.label?.run { { SingleLineText(text = state.label) } },
        placeholder = state.placeholder?.let { { SingleLineText(text = state.placeholder) } },
        value = state.textFieldValue,
        onValueChange = onValueChange,
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
        enabled = state.isEnabled
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

const val CANCEL_ICON = "cancel Icon"

@Stable
@OptIn(ExperimentalComposeUiApi::class)
interface MaxWidthTextFieldState {
    // mutable
    val textFieldValue: TextFieldValue
    val error: Int?
    val isError: Boolean
    val hasFocus: Boolean
    val isVisible: Boolean

    // immutable
    @get:StringRes
    val label: Int?

    @get:StringRes
    val placeholder: Int?

    val isEnabled: Boolean
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
        if (trailingIcon != null || cancelIconEnabled) trailingIcon?.run { invoke() } ?: CancelIcon(
            onValueChange = onValueChange
        )
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
                size = 20.dp,
                contentDescription = CANCEL_ICON
            )
        }
    }
}

open class MutableMaxWidthTextFieldState(
    initialText: String,
    initialError: Int? = null,
    initialHasFocus: Boolean = false,
    initialVisibility: Boolean,
    final override val keyboardRoute: KeyboardRoute,
    final override val imeActionRoute: ImeActionRoute,
    override val isEnabled: Boolean,
    override val label: Int?,
    override val placeholder: Int?,
    override val showKeyboard: Boolean,
    override val cancelIconEnabled: Boolean,
    private val blockBlank: Boolean
) : MaxWidthTextFieldState {
    override var textFieldValue by mutableStateOf(TextFieldValue(initialText))
    override var error: Int? by mutableStateOf(initialError)
    override val isError by derivedStateOf { error != null }
    override var hasFocus by mutableStateOf(initialHasFocus)
    override var isVisible by mutableStateOf(initialVisibility)

    private val keyboardType = when (keyboardRoute) {
        TEXT -> KeyboardType.Text
        EMAIL -> KeyboardType.Email
        PASSWORD -> KeyboardType.Password
    }
    private val imeAction = when (imeActionRoute) {
        NEXT -> ImeAction.Next
        DONE -> ImeAction.Done
    }

    override val keyboardOptions =
        KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction)

    override val focusRequester: FocusRequester = FocusRequester()

    fun onValueChange(newTextField: TextFieldValue) {
        if (blockBlank) {
            val newText = newTextField.text.trim()
            if (textFieldValue.text != newText) error = null
            textFieldValue = newTextField.copy(text = newText)
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
                it.isEnabled,
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
                isEnabled = it[3] as Boolean,
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
    initialVisibility: Boolean = true,
    isEnabled: Boolean = true,
    keyboardRoute: KeyboardRoute = TEXT,
    imeActionRoute: ImeActionRoute = DONE,
    @StringRes label: Int? = null,
    @StringRes placeholder: Int? = null,
    showKeyboard: Boolean = false,
    cancelIconEnabled: Boolean = true,
    blockBlank: Boolean = false
): MutableMaxWidthTextFieldState = rememberSaveable(
    saver = MutableMaxWidthTextFieldState.Saver, inputs = arrayOf(
        initialInput,
        isEnabled,
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
        isEnabled = isEnabled,
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
    var showedKeyboard by rememberSaveable { mutableStateOf(false) }
    if (state.showKeyboard && !showedKeyboard) LaunchedEffect(key1 = Unit) {
        if (state.showKeyboard) state.showKeyboard(
            scope = this, keyboardController = keyboardController
        )
        showedKeyboard = true
    }
}

@Composable
private fun TextFieldEmit(state: MaxWidthTextFieldState, onInputChange: (String) -> Unit) {
    val currentOnInputChange by rememberUpdatedState(newValue = onInputChange)
    LaunchedEffect(key1 = state) {
        snapshotFlow { state.textFieldValue.text.trim() }.collect(currentOnInputChange)
    }
}

@Composable
fun SetTextFieldError(error: () -> Int?, collect: (error: Int?) -> Unit) {
    LaunchedEffect(key1 = error, key2 = collect) { snapshotFlow { error() }.collect(collect) }
}