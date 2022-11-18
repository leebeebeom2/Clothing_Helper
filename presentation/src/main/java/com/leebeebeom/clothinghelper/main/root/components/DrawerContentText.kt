package com.leebeebeom.clothinghelper.main.root.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.leebeebeom.clothinghelper.base.composables.SingleLineText

@Composable
fun DrawerContentText(modifier: Modifier = Modifier, text: () -> String, style: TextStyle) {
    DrawerContentText(modifier = modifier, text = text(), style = style)
}

@Composable
fun DrawerContentText(modifier: Modifier = Modifier, text: String, style: TextStyle) {
    SingleLineText(
        modifier = modifier,
        text = text,
        style = style
    )
}