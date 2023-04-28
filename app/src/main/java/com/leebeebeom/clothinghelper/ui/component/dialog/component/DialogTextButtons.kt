package com.leebeebeom.clothinghelper.ui.component.dialog.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NoLiveLiterals
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.SingleLineText

@Composable
fun DialogTextButtons(
    @StringRes positiveButtonText: Int = R.string.positive,
    positiveButtonEnabled: () -> Boolean,
    onPositiveButtonClick: () -> Unit,
    onDismiss: () -> Unit,
    positiveButtonColor: Color,
    cancelButtonColor: Color,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        val weightModifier = Modifier.weight(1f)

        DialogTextButton(
            modifier = weightModifier,
            text = R.string.cancel,
            textColor = cancelButtonColor,
            onClick = onDismiss
        )
        DialogTextButton(
            modifier = weightModifier,
            text = positiveButtonText,
            textColor = positiveButtonColor,
            enabled = positiveButtonEnabled,
            onClick = {
                onPositiveButtonClick()
                onDismiss()
            })
    }
}

@NoLiveLiterals
@Composable
fun DialogTextButton(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    textColor: Color = Color.Unspecified,
    enabled: () -> Boolean = { true },
    onClick: () -> Unit
) {
    val localEnabled by remember(enabled) { derivedStateOf(enabled) }

    TextButton(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        enabled = localEnabled
    ) {
        SingleLineText(
            text = text,
            style = MaterialTheme.typography.body1.copy(color = textColor),
        )
    }
}