package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.leebeebeom.clothinghelper.ui.component.SingleLineText

@Composable
fun RowScope.DrawerText(
    modifier: Modifier = Modifier, @StringRes text: Int, style: TextStyle
) {
    SingleLineText(modifier = modifier.weight(1f), text = text, style = style)
}