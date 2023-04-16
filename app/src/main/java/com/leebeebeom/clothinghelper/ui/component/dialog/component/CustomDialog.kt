package com.leebeebeom.clothinghelper.ui.component.dialog.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.ui.component.HeightSpacer
import com.leebeebeom.clothinghelper.ui.component.SingleLineText
import com.leebeebeom.clothinghelper.ui.theme.Black
import com.leebeebeom.clothinghelper.ui.theme.WhiteSmoke

@Composable
fun CustomDialog(
    onDismiss: () -> Unit,
    @StringRes title: Int,
    content: @Composable () -> Unit,
    positiveButtonEnabled: () -> Boolean = { true },
    onPositiveButtonClick: () -> Unit,
    positiveButtonColor: Color = Color.Unspecified,
    cancelButtonColor: Color = MaterialTheme.colors.error,
) {
    MaterialTheme(colors = MaterialTheme.colors.copy(surface = WhiteSmoke, onSurface = Black)) {
        DialogRoot(onDismiss = onDismiss) {

            SingleLineText(
                text = title,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
            )
            HeightSpacer(dp = 12)
            content()

            DialogTextButtons(
                positiveButtonEnabled = positiveButtonEnabled,
                onPositiveButtonClick = onPositiveButtonClick,
                onDismiss = onDismiss,
                positiveButtonColor = positiveButtonColor,
                cancelButtonColor = cancelButtonColor
            )
        }
    }
}