package com.leebeebeom.clothinghelper.ui.main.dialogs.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.leebeebeom.clothinghelper.composable.SimpleHeightSpacer

@Composable
fun DialogRoot(onDismiss: () -> Unit, content: @Composable () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = MaterialTheme.colors.surface,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(start = 40.dp, end = 40.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            ) {
                content()
                SimpleHeightSpacer(dp = 20)
            }
        }
    }
}