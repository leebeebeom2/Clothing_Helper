package com.leebeebeom.clothinghelper.base

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
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
import kotlinx.coroutines.delay

@Composable
fun MaxWidthTextField(
    state: MaxWidthTextFieldState,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()

    Column {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester = focusRequester)
                .onFocusChanged(onFocusChanged = state::onFocusChanged),
            value = state.textFiled,
            onValueChange = state::onValueChange,
            label = { Text(text = stringResource(id = state.label)) },
            placeholder = { Text(text = stringResource(id = state.placeholder)) },
            isError = state.isError,
            visualTransformation = visualTransformation,
            singleLine = true,
            maxLines = 1,
            keyboardOptions = state.keyboardOptions,
            trailingIcon = trailingIcon,
            keyboardActions =
            if (state.keyboardOptions.imeAction == ImeAction.Done) KeyboardActions(onDone = { focusManager.clearFocus() })
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
    if (state.showKeyboardEnabled) ShowKeyboard(focusRequester)
}


@Composable
private fun ErrorText(@StringRes error: Int?) {
    var errorRes by remember { mutableStateOf(error) }
    if (error != null) errorRes = error

    AnimatedVisibility(
        visible = error != null,
        enter = expandVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            expandFrom = Alignment.Bottom
        ),
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = 100),
            shrinkTowards = Alignment.Top
        )
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

open class MaxWidthTextFieldState(
    @StringRes val label: Int,
    @StringRes val placeholder: Int = R.string.empty,
    text: String = "",
    error: Int? = null,
    val showKeyboardEnabled: Boolean = false,
    val keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
) {
    var textFiled by mutableStateOf(TextFieldValue(text))
        private set
    var error: Int? by mutableStateOf(error)
        private set

    val isError get() = error != null

    open fun onValueChange(newText: TextFieldValue) {
        textFiled = newText
        updateError(null)
    }

    fun updateError(@StringRes error: Int?) {
        this.error = error
    }

    open fun onFocusChanged(focusState: FocusState) {
        if (focusState.hasFocus)
            textFiled = textFiled.copy(selection = TextRange(textFiled.text.length))
    }

    companion object {
        fun email(imeAction: ImeAction) = MaxWidthTextFieldState(
            label = R.string.email,
            placeholder = R.string.email_place_holder,
            keyboardOptions = KeyboardOptions(imeAction = imeAction)
        )

        fun password(imeAction: ImeAction) = MaxWidthTextFieldState(
            label = R.string.password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction
            )
        )

        val Saver: Saver<MaxWidthTextFieldState, *> = listSaver(
            save = {
                listOf(
                    it.label,
                    it.placeholder,
                    it.textFiled.text,
                    it.error,
                    it.showKeyboardEnabled
                )
            },
            restore = {
                MaxWidthTextFieldState(
                    it[0] as Int,
                    it[1] as Int,
                    it[2] as String,
                    it[3] as Int?,
                    it[4] as Boolean,
                )
            }
        )
    }
}

@Composable
fun rememberMaxWidthStateHolder(
    @StringRes label: Int,
    @StringRes placeholder: Int = R.string.empty,
    text: String = "",
    error: Int? = null,
    showKeyboardEnabled: Boolean = false
) = rememberSaveable(saver = MaxWidthTextFieldState.Saver) {
    MaxWidthTextFieldState(
        label = label,
        placeholder = placeholder,
        text = text,
        error = error,
        showKeyboardEnabled = showKeyboardEnabled
    )
}