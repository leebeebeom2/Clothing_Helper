package com.leebeebeom.clothinghelper.ui.component.dialog

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableSet

@Composable // skippable
fun AddDialog(
    @StringRes label: Int,
    @StringRes placeHolder: Int,
    @StringRes title: Int,
    @StringRes existNameError: Int,
    names: () -> ImmutableSet<String>,
    onPositiveButtonClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    EditNameDialog(
        label = label,
        placeHolder = placeHolder,
        title = title,
        existNameError = existNameError,
        names = names,
        onPositiveButtonClick = onPositiveButtonClick,
        onDismiss = onDismiss,
        initialName = { "" }
    )
}