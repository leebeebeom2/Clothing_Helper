package com.leebeebeom.clothinghelper.ui.main.dialogs

import androidx.annotation.StringRes
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.leebeebeom.clothinghelper.ui.main.dialogs.composables.TextFieldDialog
import kotlinx.collections.immutable.ImmutableList

@Composable // skippable
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
    var input by rememberSaveable { mutableStateOf(initialName) }
    var error: Int? by rememberSaveable { mutableStateOf(null) }
    val positiveButtonEnabled by remember { derivedStateOf { input.isNotBlank() && error == null } }
    val localNames = names()
    val onInputChange = remember<(String) -> Unit> {
        { newName ->
            input = newName
            error = null
            if (localNames.contains(newName)) error = existNameError
        }
    }

    TextFieldDialog(
        label = label,
        placeHolder = placeHolder,
        title = title,
        initialText = initialName,
        positiveButtonEnabled = { positiveButtonEnabled },
        onPositiveButtonClick = { onPositiveButtonClick(input) },
        onDismiss = onDismiss,
        onInputChange = onInputChange,
        error = { error }
    )
}