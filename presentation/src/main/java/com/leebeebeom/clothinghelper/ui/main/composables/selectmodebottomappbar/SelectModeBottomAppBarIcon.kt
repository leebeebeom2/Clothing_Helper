package com.leebeebeom.clothinghelper.ui.main.composables.selectmodebottomappbar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.leebeebeom.clothinghelper.composable.CustomIconButton
import com.leebeebeom.clothinghelper.composable.SingleLineText

@Composable
fun SelectModeBottomAppBarIcon(
    visible: () -> Boolean, onClick: () -> Unit, @DrawableRes drawable: Int, @StringRes text: Int
) {
    if (visible())
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CustomIconButton(onClick = onClick, drawable = drawable)
            SingleLineText(text = text, style = MaterialTheme.typography.caption)
        }
}