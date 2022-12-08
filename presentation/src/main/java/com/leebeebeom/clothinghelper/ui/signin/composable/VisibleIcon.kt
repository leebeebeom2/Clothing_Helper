package com.leebeebeom.clothinghelper.ui.signin.composable

import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.composable.CustomIconButton

@Composable
fun VisibleIcon(isVisible: () -> Boolean, onClick: () -> Unit) {
    CustomIconButton(
        drawable = if (isVisible()) R.drawable.ic_visibility_off else R.drawable.ic_visibility,
        tint = LocalContentColor.current.copy(0.4f),
        onClick = onClick,
        size = 24.dp
    )
}