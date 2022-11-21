package com.leebeebeom.clothinghelper.base.composables

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.leebeebeom.clothinghelper.R

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun CircleCheckBox(
    modifier: Modifier = Modifier, isChecked: () -> Boolean, onClick: () -> Unit, size: Dp
) {
    val painter = rememberAnimatedVectorPainter(
        animatedImageVector = AnimatedImageVector.animatedVectorResource(
            id = R.drawable.check_anim
        ), atEnd = isChecked()
    )
    CustomIconButton(
        modifier = modifier,
        onClick = onClick,
        painter = painter,
        tint = LocalContentColor.current.copy(0.7f),
        size = size
    )
}