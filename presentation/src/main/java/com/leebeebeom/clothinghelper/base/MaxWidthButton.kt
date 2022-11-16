package com.leebeebeom.clothinghelper.base

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MaxWidthButton(
    @StringRes text: Int,
    enabled: () -> Boolean,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    icon: @Composable () -> Unit = {},
    onClick: () -> Unit,
    focusManager: FocusManager = LocalFocusManager.current
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(52.dp), onClick = {
            onClick()
            focusManager.clearFocus()
        }, colors = colors, enabled = enabled()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            SingleLineText(
                text = text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
        }
    }
}