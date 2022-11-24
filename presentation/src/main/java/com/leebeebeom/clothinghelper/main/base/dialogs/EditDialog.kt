package com.leebeebeom.clothinghelper.main.base.dialogs

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.main.base.dialogs.composables.BaseTextFieldDialogState
import com.leebeebeom.clothinghelper.main.base.dialogs.composables.TextFieldDialog
import com.leebeebeom.clothinghelperdomain.model.container.BaseContainer
import kotlinx.collections.immutable.ImmutableList

@Composable
fun EditDialog(
    @StringRes label: Int,
    @StringRes placeHolder: Int,
    @StringRes title: Int,
    items: () -> ImmutableList<BaseContainer>,
    @StringRes existNameError: Int,
    onDismiss: () -> Unit,
    onPositiveButtonClick: (String) -> Unit,
    show: () -> Boolean,
    initialName: () -> String
) {
    if (show()) {
        val names by remember { derivedStateOf { items().map { it.name } } }
        val state =
            rememberSaveable(saver = EditDialogState.Saver) { EditDialogState(initialName = initialName()) }

        TextFieldDialog(
            label = label,
            placeHolder = placeHolder,
            title = title,
            error = { state.error },
            textFieldValue = { state.textFieldValue },
            positiveButtonEnabled = { state.positiveButtonEnabled },
            onValueChange = {
                state.onValueChange(it)
                if (names.contains(it.text.trim())) state.updateError(existNameError)
                if (state.initialName == it.text) state.updateError(null)
            },
            onFocusChanged = state::onFocusChange,
            onPositiveButtonClick = { onPositiveButtonClick(state.text.trim()) },
            onDismiss = onDismiss
        )
    }
}

class EditDialogState(
    val initialName: String,
    initialText: String = initialName,
    initialError: Int? = null
) :
    BaseTextFieldDialogState(initialText, initialError) {
    override fun onValueChange(newTextFiled: TextFieldValue) {
        super.onValueChange(newTextFiled)
        if (newTextFiled.text.trim() == initialName) error = null
    }

    override val positiveButtonEnabled by derivedStateOf { super.positiveButtonEnabled && initialName != text.trim() }

    fun onFocusChange(newFocusState: FocusState) {
        if (newFocusState.hasFocus) textFieldValue =
            textFieldValue.copy(selection = TextRange(0, text.length))
    }

    companion object {
        val Saver: Saver<EditDialogState, *> = listSaver(
            save = { listOf(it.initialName, it.text, it.error) },
            restore = {
                EditDialogState(
                    it[0] as String,
                    it[1] as String,
                    it[2] as? Int
                )
            }
        )
    }
}