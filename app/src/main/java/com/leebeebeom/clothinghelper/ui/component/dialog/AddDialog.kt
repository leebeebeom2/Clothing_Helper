package com.leebeebeom.clothinghelper.ui.component.dialog

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import kotlinx.collections.immutable.ImmutableSet

@Composable
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
        positiveButtonText = R.string.add,
        names = names,
        onPositiveButtonClick = onPositiveButtonClick,
        onDismiss = onDismiss,
        initialName = { "" }
    )
}