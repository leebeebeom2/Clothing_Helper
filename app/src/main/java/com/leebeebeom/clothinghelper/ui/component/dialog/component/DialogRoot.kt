package com.leebeebeom.clothinghelper.ui.component.dialog.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.leebeebeom.clothinghelper.ui.component.HeightSpacer

const val DialogRootTag = "dialog root"

@Composable // skippable
fun DialogRoot(onDismiss: () -> Unit, content: @Composable () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = MaterialTheme.colors.surface,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                    .testTag(DialogRootTag)
            ) {
                content()
                HeightSpacer(dp = 20)
            }
        }
    }
}