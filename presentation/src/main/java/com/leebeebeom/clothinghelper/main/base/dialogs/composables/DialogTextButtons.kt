package com.leebeebeom.clothinghelper.main.base.dialogs.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.SingleLineText

@Composable
fun DialogTextButtons(
    positiveTextColor: Color = Color.Unspecified,
    cancelTextColor: Color = MaterialTheme.colors.error,
    positiveButtonEnabled: () -> Boolean,
    onPositiveButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Row {
        DialogTextButton(text = R.string.cancel, textColor = cancelTextColor, onClick = onDismiss)
        DialogTextButton(text = R.string.check,
            textColor = positiveTextColor,
            enabled = positiveButtonEnabled,
            onClick = {
                onPositiveButtonClick()
                onDismiss()
            })
    }
}

@Composable
fun RowScope.DialogTextButton(
    @StringRes text: Int,
    textColor: Color = Color.Unspecified,
    enabled: () -> Boolean = { true },
    onClick: () -> Unit
) {
    Box(modifier = Modifier.weight(1f)) {
        TextButton(
            modifier = Modifier.align(Alignment.Center), onClick = onClick, enabled = enabled()
        ) {
            SingleLineText(
                text = text,
                style = MaterialTheme.typography.subtitle1.copy(color = textColor),
            )
        }
    }
}