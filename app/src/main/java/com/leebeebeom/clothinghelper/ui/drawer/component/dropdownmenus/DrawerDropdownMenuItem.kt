package com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.ui.component.SingleLineText

@Composable
fun DrawerDropdownMenuItem(@StringRes text: Int, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.padding(horizontal = 4.dp),
        shape = MaterialTheme.shapes.small,
    ) {
        SingleLineText(
            text = text,
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(vertical = 12.dp)
                .padding(start = 12.dp, end = 16.dp)
                .widthIn(52.dp)
                .fillMaxSize(),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Start
        )
    }
}