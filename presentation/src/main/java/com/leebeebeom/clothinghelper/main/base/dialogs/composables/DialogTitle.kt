package com.leebeebeom.clothinghelper.main.base.dialogs.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.composables.SingleLineText

@Composable
fun DialogTitle(@StringRes text: Int) {
    SingleLineText(
        text = text,
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
    )
}