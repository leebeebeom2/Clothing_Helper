package com.leebeebeom.clothinghelper.base

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MaxWidthButton(
    state: MaxWidthButtonState,
    onClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(52.dp), onClick = {
            focusManager.clearFocus()
            onClick()
        }, colors = state.colors(), enabled = state.enabled
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            state.icon?.invoke()
            Text(
                text = stringResource(id = state.text),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center
            )
        }
    }
}

class MaxWidthButtonState(
    @StringRes val text: Int,
    val colors: @Composable () -> ButtonColors = { ButtonDefaults.buttonColors() },
    val icon: @Composable (() -> Unit)? = null,
) {
    var enabled by mutableStateOf(true)
        private set

    fun updateEnabled(enabled: Boolean) {
        this.enabled = enabled
    }
}