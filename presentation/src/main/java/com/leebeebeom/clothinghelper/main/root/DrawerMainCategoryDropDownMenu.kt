package com.leebeebeom.clothinghelper.main.root

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.DropDownMenuRoot

@Composable
fun DrawerMainCategoryDropDownMenu(
    show: () -> Boolean,
    onDismiss: () -> Unit,
    onClick: () -> Unit
) {
    DropDownMenuRoot(show = show, onDismiss = onDismiss) {
        DropdownMenuItem(
            onClick = {
                onClick()
                onDismiss()
            },
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.add_category),
                style = MaterialTheme.typography.body2
            )
        }
    }
}