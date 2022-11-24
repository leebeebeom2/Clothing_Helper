package com.leebeebeom.clothinghelper.main.root.contents.dropdownmenus

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.composables.SingleLineText

@Composable
fun DrawerDropdownMenuItem(
    @StringRes text: Int,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    SingleLineText(
        text = text, modifier =
        Modifier
            .clickable(
                onClick = {
                    onClick()
                    onDismiss()
                }
            )
            .padding(vertical = 10.dp)
            .padding(start = 12.dp, end = 24.dp)
            .fillMaxSize(),
        style = MaterialTheme.typography.subtitle2,
        textAlign = TextAlign.Start
    )
}