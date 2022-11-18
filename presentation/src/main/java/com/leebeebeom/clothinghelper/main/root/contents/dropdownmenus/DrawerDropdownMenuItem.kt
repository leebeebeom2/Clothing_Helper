package com.leebeebeom.clothinghelper.main.root.contents.dropdownmenus

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.composables.SingleLineText

@Composable
fun DrawerDropdownMenuItem(
    @StringRes text: Int,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenuItem(
        onClick = {
            onClick()
            onDismiss()
        }, contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        SingleLineText(
            text = stringResource(id = text),
            style = MaterialTheme.typography.body2
        )
    }
}