package com.leebeebeom.clothinghelper.base.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes drawable: Int,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    contentDescription: String? = null
) {
    CustomIconButton(
        modifier = modifier,
        onClick = onClick,
        painter = painterResource(id = drawable),
        tint = tint,
        contentDescription = contentDescription
    )
}

@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    painter: Painter,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    contentDescription: String? = null
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(2.dp)
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint
        )
    }
}