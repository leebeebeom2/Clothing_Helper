package com.leebeebeom.clothinghelper.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp

@Composable // skippable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes drawable: Int,
    size: Dp = Dp.Unspecified,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    @StringRes text: Int? = null,
    contentDescription: String? = null
) {
    CustomIconButton(
        modifier = modifier,
        onClick = onClick,
        painter = painterResource(id = drawable),
        tint = tint,
        size = size,
        text = text,
        contentDescription = contentDescription
    )
}

@Composable // skippable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    painter: Painter,
    size: Dp = Dp.Unspecified,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    @StringRes text: Int? = null,
    contentDescription: String? = null
) {
    IconButton(modifier = modifier, onClick = onClick) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painter,
                contentDescription = contentDescription,
                modifier = Modifier.size(size),
                tint = tint
            )
            text?.let {
                SingleLineText(text = it, style = MaterialTheme.typography.body2)
            }
        }
    }
}