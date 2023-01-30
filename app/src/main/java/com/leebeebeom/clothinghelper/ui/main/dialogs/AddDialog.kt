package com.leebeebeom.clothinghelper.ui.main.dialogs

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AddDialog(
    @StringRes label: Int,
    @StringRes placeHolder: Int,
    @StringRes title: Int,
    @StringRes existNameError: Int,
    names: () -> ImmutableList<String>,
    onPositiveButtonClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    EditDialog(
        label = label,
        placeHolder = placeHolder,
        title = title,
        existNameError = existNameError,
        names = names,
        onPositiveButtonClick = onPositiveButtonClick,
        onDismiss = onDismiss,
        initialName = ""
    )
}