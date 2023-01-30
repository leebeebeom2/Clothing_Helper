package com.leebeebeom.clothinghelper.ui.main.dialogs.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.ui.components.HeightSpacer
import com.leebeebeom.clothinghelper.ui.components.MaxWidthTextFieldWithError
import com.leebeebeom.clothinghelper.ui.components.SingleLineText
import com.leebeebeom.clothinghelper.ui.components.rememberMaxWidthTextFieldState

@Composable
fun TextFieldDialog(
    state: TextFieldDialogState,
    positiveButtonEnabled: () -> Boolean,
    onPositiveButtonClick: () -> Unit,
    onDismiss: () -> Unit,
    onInputChange: (String) -> Unit
) {
    DialogRoot(onDismiss = onDismiss) {
        SingleLineText(
            text = state.title,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )

        val textFieldState = rememberMaxWidthTextFieldState(
            label = state.label,
            placeholder = state.placeHolder,
            showKeyboard = true,
            initialInput = state.input
        )

        MaxWidthTextFieldWithError(
            state = textFieldState,
            onValueChange = textFieldState::onValueChange,
            onFocusChanged = textFieldState::onFocusChanged,
            onInputChange = onInputChange
        )
        HeightSpacer(dp = 12)

        DialogTextButtons(
            positiveButtonEnabled = positiveButtonEnabled,
            onPositiveButtonClick = onPositiveButtonClick,
            onDismiss = onDismiss
        )
    }
}

@Stable
interface TextFieldDialogState {
    @get:StringRes
    val label: Int

    @get:StringRes
    val placeHolder: Int?

    @get:StringRes
    val title: Int

    val error: Int?
    val input: String
    val positiveButtonEnabled: Boolean
}

@Stable
class MutableTextFieldDialogState(
    initialError: Int? = null,
    initialInput: String = "",
    @StringRes override val label: Int,
    @StringRes override val placeHolder: Int?,
    @StringRes override val title: Int,
) : TextFieldDialogState {
    override var error: Int? by mutableStateOf(initialError)
    override var input by mutableStateOf(initialInput)
    override val positiveButtonEnabled by derivedStateOf { input.isNotBlank() && error == null }

    fun onInputChange(input: String) {
        this.input = input
        error = null
    }

    companion object {
        val Saver: Saver<MutableTextFieldDialogState, *> = listSaver(save = {
            listOf(
                it.error, it.input, it.label, it.placeHolder, it.title
            )
        }, restore = {
            MutableTextFieldDialogState(
                initialError = it[0] as Int?,
                initialInput = it[1] as String,
                label = it[2] as Int,
                placeHolder = it[3] as Int?,
                title = it[4] as Int,
            )
        })
    }
}

@Composable
fun rememberTextFieldDialog(
    initialError: () -> Int?,
    initialInput: String,
    @StringRes label: Int,
    @StringRes placeHolder: Int?,
    @StringRes title: Int
): MutableTextFieldDialogState =
    rememberSaveable(
        inputs = arrayOf(
            initialError,
            initialInput,
            label,
            placeHolder,
            title
        ),
        saver = MutableTextFieldDialogState.Saver
    ) {
        MutableTextFieldDialogState(
            initialError = initialError(),
            initialInput = initialInput,
            label = label,
            placeHolder = placeHolder,
            title = title
        )
    }