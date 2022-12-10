package com.leebeebeom.clothinghelper.ui.main.dialogs.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.composable.SingleLineText

@Composable
fun DialogTextButtons(
    positiveButtonEnabled: () -> Boolean,
    onPositiveButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        DialogTextButton(
            text = R.string.cancel,
            textColor = MaterialTheme.colors.error,
            onClick = onDismiss
        )
        DialogTextButton(text = R.string.check,
            textColor = Color.Unspecified,
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
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            onClick = onClick,
            enabled = enabled()
        ) {
            SingleLineText(
                text = text,
                style = MaterialTheme.typography.body1.copy(color = textColor),
            )
        }
    }
}