package com.leebeebeom.clothinghelper.ui.main.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.CustomIconButton
import com.leebeebeom.clothinghelper.ui.component.IconWrapper
import com.leebeebeom.clothinghelper.ui.util.noRippleClickable

@Composable
fun CircleCheckBox(
    modifier: Modifier = Modifier, isChecked: () -> Boolean, onClick: () -> Unit, size: Dp
) {
    val painter = rememberCheckBoxPainter(res = R.drawable.check_anim_circle, isChecked = isChecked)

    CustomIconButton(
        modifier = modifier,
        onClick = onClick,
        painter = painter,
        tint = checkBoxIconTint(),
        size = size
    )
}

@Composable
fun SquareCheckBox(
    modifier: Modifier = Modifier, isChecked: () -> Boolean, onClick: () -> Unit, size: Dp
) {
    val painter =
        rememberCheckBoxPainter(res = R.drawable.check_anime_square, isChecked = isChecked)
    IconWrapper(
        modifier =
        modifier
            .size(size)
            .noRippleClickable { onClick() }, painter = painter,
        tint = checkBoxIconTint()
    )
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun rememberCheckBoxPainter(@DrawableRes res: Int, isChecked: () -> Boolean) =
    rememberAnimatedVectorPainter(
        animatedImageVector = AnimatedImageVector.animatedVectorResource(
            id = res
        ), atEnd = isChecked()
    )

@Composable
fun checkBoxIconTint() = LocalContentColor.current.copy(0.7f)