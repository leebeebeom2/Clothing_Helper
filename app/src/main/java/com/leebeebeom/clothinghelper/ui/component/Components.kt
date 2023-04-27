package com.leebeebeom.clothinghelper.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
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

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun AnimatedIconWrapper(
    modifier: Modifier = Modifier,
    @DrawableRes drawable: Int,
    atEnd: () -> Boolean,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {
    val painter = rememberAnimatedVectorPainter(
        AnimatedImageVector.animatedVectorResource(id = drawable),
        atEnd = atEnd()
    )

    Icon(
        modifier = modifier,
        painter = painter,
        tint = tint,
        contentDescription = null
    )
}

@Composable
fun IconWrapper(
    modifier: Modifier = Modifier,
    @DrawableRes drawable: Int,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {
    Icon(
        modifier = modifier,
        painter = painterResource(id = drawable),
        tint = tint,
        contentDescription = null
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