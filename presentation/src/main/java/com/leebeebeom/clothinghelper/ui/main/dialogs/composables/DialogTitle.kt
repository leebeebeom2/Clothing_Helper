package com.leebeebeom.clothinghelper.ui.main.dialogs.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.composable.SingleLineText

@Composable
fun DialogTitle(@StringRes text: Int) {
    SingleLineText(
        text = text,
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
    )
}