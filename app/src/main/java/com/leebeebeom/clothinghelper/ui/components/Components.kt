package com.leebeebeom.clothinghelper.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun IconWrapper(
    modifier: Modifier = Modifier,
    @DrawableRes drawable: Int,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {
    Icon(
        modifier = modifier,
        painter = painterResource(id = drawable),
        contentDescription = null,
        tint = tint
    )
}

@Composable
fun HeightSpacer(dp: Int) {
    Spacer(modifier = Modifier.height(dp.dp))
}

@Composable
fun WidthSpacer(dp: Int) {
    Spacer(modifier = Modifier.width(dp.dp))
}