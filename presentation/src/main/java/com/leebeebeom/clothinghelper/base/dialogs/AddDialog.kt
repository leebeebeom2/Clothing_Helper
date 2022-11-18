package com.leebeebeom.clothinghelper.base.dialogs

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.base.dialogs.composables.BaseTextFieldDialogState
import com.leebeebeom.clothinghelper.base.dialogs.composables.TextFieldDialog
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AddDialog(
    @StringRes label: Int,
    @StringRes placeHolder: Int,
    @StringRes title: Int,
    state: AddDialogState,
    names: () -> ImmutableList<String>,
    @StringRes existNameError: Int,
    onPositiveButtonClick: (String) -> Unit
) {
    TextFieldDialog(
        label = label,
        placeHolder = placeHolder,
        title = title,
        error = { state.error },
        textFieldValue = { state.textFieldValue },
        positiveButtonEnabled = { state.positiveButtonEnabled },
        showDialog = { state.showDialog },
        onValueChange = {
            state.onValueChange(it)
            if (names().contains(it.text.trim())) state.updateError(existNameError)
        },
        onFocusChanged = state::onFocusChange,
        onDismiss = state::onDismiss,
        onPositiveButtonClick = { onPositiveButtonClick(state.text.trim()) },
        onCancelIconClick = state::initTextFieldValue
    )
}

class AddDialogState(
    initialText: String = "", initialError: Int? = null, initialShowDialog: Boolean = false
) : BaseTextFieldDialogState(initialText, initialError) {
    var showDialog by mutableStateOf(initialShowDialog)
        private set

    fun showDialog() {
        showDialog = true
    }

    fun onDismiss() {
        showDialog = false
        text = ""
        textFieldValue = TextFieldValue("")
        error = null
    }

    fun onFocusChange(newFocusState: FocusState) {
        if (newFocusState.hasFocus) textFieldValue =
            textFieldValue.copy(selection = TextRange(text.length))
    }

    companion object {
        val Saver: Saver<AddDialogState, *> =
            listSaver(save = { listOf(it.text, it.error, it.showDialog) }, restore = {
                AddDialogState(
                    it[0] as String, it[1] as? Int, it[2] as Boolean
                )
            })
    }
}