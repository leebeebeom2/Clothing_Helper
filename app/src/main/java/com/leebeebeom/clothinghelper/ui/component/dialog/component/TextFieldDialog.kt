package com.leebeebeom.clothinghelper.ui.component.dialog.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.ui.component.HeightSpacer
import com.leebeebeom.clothinghelper.ui.component.MaxWidthTextFieldState
import com.leebeebeom.clothinghelper.ui.component.SingleLineText
import com.leebeebeom.clothinghelper.ui.component.StatefulMaxWidthTextFieldWithCancelIcon

@Composable
fun TextFieldDialog(
    state: TextFieldDialogState,
    @StringRes label: Int,
    @StringRes placeHolder: Int?,
    @StringRes title: Int,
    error: () -> Int?,
    onPositiveButtonClick: () -> Unit,
    onDismiss: () -> Unit,
    onInputChange: (String) -> Unit,
    dialogMaxWidthTextFieldState: DialogMaxWidthTextFieldState = rememberDialogMaxWidthTextFieldState(
        initialText = state.initialText
    )
) {
    DialogRoot(onDismiss = onDismiss) {
        val localError by remember { derivedStateOf(error) }
        val positiveButtonEnabled by remember {
            derivedStateOf { state.inputChanged && state.input.isNotBlank() && localError == null }
        }

        SingleLineText(
            text = title,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )

        StatefulMaxWidthTextFieldWithCancelIcon(
            state = dialogMaxWidthTextFieldState,
            label = label,
            placeholder = placeHolder,
            error = error,
            onInputChange = onInputChange,
            getFocus = true
        )

        HeightSpacer(dp = 12)

        DialogTextButtons(
            positiveButtonEnabled = { positiveButtonEnabled },
            onPositiveButtonClick = onPositiveButtonClick,
            onDismiss = onDismiss
        )
    }
}

@Composable
fun rememberDialogMaxWidthTextFieldState(initialText: String) =
    rememberSaveable(saver = DialogMaxWidthTextFieldState.Saver) {
        DialogMaxWidthTextFieldState(
            initialText = initialText
        )
    }

class DialogMaxWidthTextFieldState(initialText: String) : MaxWidthTextFieldState( // stable
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

class MutableTextFieldDialogState(override val initialText: String = "") : TextFieldDialogState {
    override var input by mutableStateOf(initialText)
        private set
    override val inputChanged by derivedStateOf { input != initialText }

    fun onInputChange(newInput: String) {
        input = newInput
    }

    companion object {
        val Saver =
            listSaver(save = { listOf(it.input) }, restore = { MutableTextFieldDialogState(it[0]) })
    }
}

@Composable
fun rememberTextFieldDialogState(initialText: String) =
    rememberSaveable(saver = MutableTextFieldDialogState.Saver) {
        MutableTextFieldDialogState(initialText = initialText)
    }