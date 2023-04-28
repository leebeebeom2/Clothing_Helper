package com.leebeebeom.clothinghelper.ui.component.dialog.component

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.ui.component.MaxWidthTextFieldWithErrorAndCancelIcon
import com.leebeebeom.clothinghelper.ui.component.MutableMaxWidthTextFieldState

@Composable
fun TextFieldDialog(
    state: TextFieldDialogState,
    @StringRes label: Int,
    @StringRes placeHolder: Int? = null,
    @StringRes title: Int,
    @StringRes positiveButtonText: Int,
    error: () -> Int?,
    onPositiveButtonClick: () -> Unit,
    onDismiss: () -> Unit,
    onInputChange: (String) -> Unit
) {
    val localError by remember(error) { derivedStateOf(error) }
    val positiveButtonEnabled by remember {
        derivedStateOf { state.inputChanged && state.input.isNotBlank() && localError == null }
    }
    val textFieldState = rememberDialogMaxWidthTextFieldState(initialText = state.initialText)

    CustomDialog(
        onDismiss = onDismiss,
        title = title,
        positiveButtonText = positiveButtonText,
        content = {
            MaxWidthTextFieldWithErrorAndCancelIcon(
                state = textFieldState,
                label = label,
                placeholder = placeHolder,
                error = error,
                onInputChange = onInputChange,
                getFocus = true,
                fixedError = true,
                onValueChange = textFieldState::onValueChange,
                onFocusChanged = textFieldState::onFocusChanged
            )
        },
        onPositiveButtonClick = onPositiveButtonClick,
        positiveButtonEnabled = { positiveButtonEnabled }
    )
}

@Composable
fun rememberDialogMaxWidthTextFieldState(initialText: String) =
    rememberSaveable(saver = DialogMaxWidthTextFieldState.Saver) {
        DialogMaxWidthTextFieldState(
            initialText = initialText
        )
    }

class DialogMaxWidthTextFieldState(initialText: String) : MutableMaxWidthTextFieldState(
    initialTextFieldValue = TextFieldValue(initialText), blockBlank = false
) {
    override fun onFocusChanged(focusState: FocusState) {
        hasFocus = focusState.hasFocus
        if (focusState.hasFocus) {
            textFieldValue = textFieldValue.copy(
                text = textFieldValue.text, selection = TextRange(0, textFieldValue.text.length)
            )
        }
    }

    companion object {
        val Saver = listSaver(save = { listOf(it.textFieldValue.text) },
            restore = { DialogMaxWidthTextFieldState(it[0]) })
    }
}

@Stable
interface TextFieldDialogState {
    val initialText: String
    val input: String
    val inputChanged: Boolean
}

class MutableTextFieldDialogState(
    override val initialText: String = "",
    initialInput: String = initialText
) : TextFieldDialogState {
    override var input by mutableStateOf(initialInput)
        private set
    override val inputChanged by derivedStateOf { input != initialText }

    fun onInputChange(newInput: String) {
        input = newInput
    }

    companion object {
        val Saver =
            listSaver(
                save = { listOf(it.initialText, it.input) },
                restore = { MutableTextFieldDialogState(it[0], it[1]) })
    }
}

@Composable
fun rememberTextFieldDialogState(initialText: String) =
    rememberSaveable(saver = MutableTextFieldDialogState.Saver) {
        MutableTextFieldDialogState(initialText = initialText)
    }