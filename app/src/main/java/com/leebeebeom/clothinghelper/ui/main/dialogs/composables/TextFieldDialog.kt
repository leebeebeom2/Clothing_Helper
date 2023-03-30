package com.leebeebeom.clothinghelper.ui.main.dialogs.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
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
import com.leebeebeom.clothinghelper.ui.component.StatefulMaxWidthTestFieldWithCancelIcon

@Composable
fun TextFieldDialog(
    @StringRes label: Int,
    @StringRes placeHolder: Int?,
    @StringRes title: Int,
    initialText: String,
    error: () -> Int?,
    positiveButtonEnabled: () -> Boolean,
    onPositiveButtonClick: () -> Unit,
    onDismiss: () -> Unit,
    onInputChange: (String) -> Unit
) {
    DialogRoot(onDismiss = onDismiss) {
        SingleLineText(
            text = title,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )

        StatefulMaxWidthTestFieldWithCancelIcon(
            state = rememberDialogMaxWidthTextFieldState(initialText = initialText),
            initialText = initialText,
            label = label,
            placeholder = placeHolder,
            error = error,
            onInputChange = onInputChange,
            getFocus = true
        )

        HeightSpacer(dp = 12)

        DialogTextButtons(
            positiveButtonEnabled = positiveButtonEnabled,
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