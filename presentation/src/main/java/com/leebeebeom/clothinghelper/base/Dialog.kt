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
fun DialogRoot(onDismissDialog: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Dialog(onDismissRequest = onDismissDialog) {
        Surface(color = MaterialTheme.colors.surface, shape = RoundedCornerShape(20.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                content = content
            )
        }
    }
}

@Composable
fun DialogTitle(@StringRes title: Int) {
    Text(
        text = stringResource(title),
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
    )
}

@Composable
fun DialogTextField(
    categoryName: String,
    @StringRes error: Int?,
    onCategoryNameChange: (String) -> Unit,
) {
    MaxWidthTextField(
        label = R.string.category,
        placeholder = R.string.category_place_holder,
        text = categoryName,
        onValueChange = onCategoryNameChange,
        error = error,
        showKeyboardEnabled = true
    )
    SimpleHeightSpacer(dp = 12)
}

@Composable
fun DialogTextButton(
    modifier: Modifier,
    @StringRes text: Int,
    textColor: Color = Color.Unspecified,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
        TextButton(
            modifier = Modifier.align(Alignment.Center),
            onClick = onClick,
            enabled = enabled
        ) {
            Text(
                text = stringResource(id = text),
                style = MaterialTheme.typography.subtitle1,
                color = textColor
            )
        }
    }
}

@Composable
fun DialogTextButtons(
    positiveButtonEnabled: Boolean,
    onPositiveButtonClick: () -> Unit,
    onDismissDialog: () -> Unit
) {
    Row {
        val weightModifier = Modifier.weight(1f)

        DialogTextButton(
            modifier = weightModifier,
            textColor = MaterialTheme.colors.error,
            text = R.string.cancel,
            onClick = onDismissDialog
        )

        DialogTextButton(
            modifier = weightModifier,
            text = R.string.check,
            enabled = positiveButtonEnabled,
            onClick = {
                onPositiveButtonClick()
                onDismissDialog()
            }
        )
    }
}