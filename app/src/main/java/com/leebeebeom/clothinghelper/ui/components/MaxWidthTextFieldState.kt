@file:OptIn(ExperimentalComposeUiApi::class)

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
    DEFAULT, DONE
}

@Composable
fun MaxWidthTextFieldWithError(
    modifier: Modifier = Modifier,
    state: MaxWidthTextFieldState,
    trailingIcon: @Composable (() -> Unit)?
) {
    Column(modifier = modifier) {
        MaxWidthTextField(state = state, trailingIcon = trailingIcon)
        ErrorText(state = state)
    }
    ShowKeyboard(state = state)
}

@Composable
private fun MaxWidthTextField(
    state: MaxWidthTextFieldState,
    trailingIcon: @Composable (() -> Unit)?,
    focusManager: FocusManager = LocalFocusManager.current
) {
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
        trailingIcon = { state.TrailingIcon(trailingIcon = trailingIcon) },
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
    val keyboardRoute: KeyboardRoute
    val imeActionRoute: ImeActionRoute
    fun onValueChange(newTextField: TextFieldValue)
    fun showKeyboard(scope: CoroutineScope, keyboardController: SoftwareKeyboardController?)
    fun onFocusChanged(focusState: FocusState)
    fun getKeyboardActions(focusManager: FocusManager): KeyboardActions

    @Composable
    fun TrailingIcon(trailingIcon: @Composable (() -> Unit)?) {
        if (trailingIcon != null || cancelIconEnabled) Row {
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
class MutableMaxWidthTextFieldState(
    initialText: String = "",
    initialError: Int? = null,
    initialHasFocus: Boolean = false,
    initialEnabled: Boolean = true,
    initialVisibility: Boolean = true,
    override val keyboardRoute: KeyboardRoute = KeyboardRoute.TEXT,
    override val imeActionRoute: ImeActionRoute = ImeActionRoute.DONE,
    override val label: Int,
    override val placeholder: Int?,
    override val showKeyboard: Boolean,
    override val cancelIconEnabled: Boolean,
) : MaxWidthTextFieldState {
    override var textFieldValue by mutableStateOf(TextFieldValue(initialText))
    override var error: Int? by mutableStateOf(initialError)
    override val hasFocus by mutableStateOf(initialHasFocus)
    override val enabled by mutableStateOf(initialEnabled)
    override val isVisible by mutableStateOf(initialVisibility)

    private val keyboardType = when (keyboardRoute) {
        KeyboardRoute.TEXT -> KeyboardType.Text
        KeyboardRoute.EMAIL -> KeyboardType.Email
        KeyboardRoute.PASSWORD -> KeyboardType.Password
    }
    private val imeAction = when (imeActionRoute) {
        ImeActionRoute.DONE -> ImeAction.Done
        ImeActionRoute.DEFAULT -> ImeAction.Default
    }

    override val keyboardOptions =
        KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction)

    override val focusRequester: FocusRequester = FocusRequester()

    override fun onValueChange(newTextField: TextFieldValue) {
        textFieldValue = newTextField
    }

    override fun showKeyboard(
        scope: CoroutineScope,
        keyboardController: SoftwareKeyboardController?
    ) {
        scope.launch {
            focusRequester.requestFocus()
            delay(100)
            keyboardController?.show()
        }
    }

    override fun onFocusChanged(focusState: FocusState) {}

    override fun getKeyboardActions(focusManager: FocusManager) =
        if (imeAction == ImeAction.Done) KeyboardActions(onDone = { focusManager.clearFocus() })
        else KeyboardActions.Default

    companion object {
        val Saver: Saver<MutableMaxWidthTextFieldState, *> = listSaver(
            save = {
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
                    it.cancelIconEnabled
                )
            },
            restore = {
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
                    cancelIconEnabled = it[10] as Boolean
                )
            }
        )
    }
}

@Composable
@NonRestartableComposable
fun rememberMaxWidthTextFieldState(
    initialText: String = "",
    initialVisibility: Boolean = true,
    @StringRes label: Int,
    @StringRes placeholder: Int? = null,
    showKeyboard: Boolean = false,
    cancelIconEnabled: Boolean = true,
): MutableMaxWidthTextFieldState = rememberSaveable(saver = MutableMaxWidthTextFieldState.Saver) {
    MutableMaxWidthTextFieldState(
        initialText = initialText,
        initialVisibility = initialVisibility,
        label = label,
        placeholder = placeholder,
        showKeyboard = showKeyboard,
        cancelIconEnabled = cancelIconEnabled
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