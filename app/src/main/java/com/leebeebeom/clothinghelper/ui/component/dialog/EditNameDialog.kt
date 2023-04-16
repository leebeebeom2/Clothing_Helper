package com.leebeebeom.clothinghelper.ui.component.dialog

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.leebeebeom.clothinghelper.ui.component.dialog.component.DialogMaxWidthTextFieldState
import com.leebeebeom.clothinghelper.ui.component.dialog.component.MutableTextFieldDialogState
import com.leebeebeom.clothinghelper.ui.component.dialog.component.TextFieldDialog
import com.leebeebeom.clothinghelper.ui.component.dialog.component.rememberDialogMaxWidthTextFieldState
import com.leebeebeom.clothinghelper.ui.component.dialog.component.rememberTextFieldDialogState
import kotlinx.collections.immutable.ImmutableSet

@Composable // skippable
fun EditNameDialog(
    @StringRes label: Int,
    @StringRes placeHolder: Int?,
    @StringRes title: Int,
    @StringRes existNameError: Int,
    names: () -> ImmutableSet<String>,
    onPositiveButtonClick: (String) -> Unit,
    onDismiss: () -> Unit,
    initialName: () -> String,
    state: MutableTextFieldDialogState = rememberTextFieldDialogState(initialText = initialName()),
    dialogMaxWidthTextFieldState: DialogMaxWidthTextFieldState = rememberDialogMaxWidthTextFieldState(
        initialText = state.initialText
    )
) {
    val localNames by remember(names) { derivedStateOf(names) }
    val error by remember {
        derivedStateOf {
            when {
                !state.inputChanged -> null
                localNames.contains(state.input.trim()) -> existNameError
                else -> null
            }
        }
    }

    TextFieldDialog(
        state = state,
        label = label,
        placeHolder = placeHolder,
        title = title,
        onPositiveButtonClick = { onPositiveButtonClick(state.input.trim()) },
        onDismiss = onDismiss,
        onInputChange = state::onInputChange,
        error = { error },
        dialogMaxWidthTextFieldState = dialogMaxWidthTextFieldState
    )
}