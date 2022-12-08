package com.leebeebeom.clothinghelper.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.theme.buttonTextStyle

@Composable
fun MaxWidthButton(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    enabled: () -> Boolean = { true },
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    icon: @Composable () -> Unit = {},
    onClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Button(
        modifier = modifier
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
                style = buttonTextStyle()
            )
        }
    }
}