package com.leebeebeom.clothinghelper.ui.main.dialogs

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.ui.main.dialogs.composables.TextFieldDialog
import com.leebeebeom.clothinghelper.ui.main.dialogs.composables.rememberTextFieldDialog
import kotlinx.collections.immutable.ImmutableList

@Composable
fun EditDialog(
    @StringRes label: Int,
    @StringRes placeHolder: Int,
    @StringRes title: Int,
    @StringRes existNameError: Int,
    names: () -> ImmutableList<String>,
    onPositiveButtonClick: (String) -> Unit,
    onDismiss: () -> Unit,
    initialName: String
) {
    var error: Int? by rememberSaveable { mutableStateOf(null) }

    val state = rememberTextFieldDialog(
        initialError = { error },
        initialInput = initialName,
        label = label,
        placeHolder = placeHolder,
        title = title
    )

    TextFieldDialog(
        state = state,
        positiveButtonEnabled = { state.positiveButtonEnabled },
        onPositiveButtonClick = { onPositiveButtonClick(state.input) },
        onDismiss = onDismiss,
        onInputChange = {
            if (names().contains(it)) error = existNameError
            else state.onInputChange(it)
        })
}