package com.leebeebeom.clothinghelper.base

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.leebeebeom.clothinghelper.R

@Composable
fun DialogRoot(onDismiss: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(color = MaterialTheme.colors.surface, shape = RoundedCornerShape(20.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            ) {
                content()
                SimpleHeightSpacer(dp = 20)
            }
        }
    }
}

@Composable
fun DialogTitle(@StringRes text: Int) {
    Text(
        text = stringResource(id = text),
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
    )
}

@Composable
fun DialogTextButtons(
    positiveTextColor: Color = Color.Unspecified,
    cancelTextColor: Color = MaterialTheme.colors.error,
    positiveButtonEnabled: Boolean,
    onPositiveButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Row {
        DialogTextButton(
            text = R.string.cancel, textColor = cancelTextColor, onClick = onDismiss
        )

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
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.weight(1f)) {
        TextButton(
            modifier = Modifier.align(Alignment.Center), onClick = onClick, enabled = enabled
        ) {
            Text(
                text = stringResource(id = text),
                style = MaterialTheme.typography.subtitle1,
                color = textColor
            )
        }
    }
}