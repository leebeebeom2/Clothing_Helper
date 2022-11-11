package com.leebeebeom.clothinghelper.main.base

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.CustomIconButton

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun AllExpandIcon(
    size: Dp,
    onClick: () -> Unit,
    tint: Color,
    isAllExpand: () -> Boolean
) {
    val painter = rememberAnimatedVectorPainter(
        animatedImageVector = AnimatedImageVector.animatedVectorResource(
            id = R.drawable.all_expand_anim
        ), atEnd = isAllExpand()
    )

    CustomIconButton(
        modifier = Modifier.size(size),
        onClick = onClick,
        painter = painter,
        tint = tint
    )
}

@Composable
fun ExpandIcon(
    modifier: Modifier = Modifier,
    isExpanded: () -> Boolean,
    onClick: () -> Unit,
    isAllExpand: () -> Boolean,
    updateIsExpand: (Boolean) -> Unit
) {
    val isAllExpandVal = isAllExpand()
    var rememberedIsAllExpand by rememberSaveable { mutableStateOf(isAllExpandVal) }
    if (isAllExpand() != rememberedIsAllExpand) {
        rememberedIsAllExpand = isAllExpandVal
        updateIsExpand(isAllExpandVal)
    }

    val rotate by animateFloatAsState(
        targetValue = if (!isExpanded()) 0f else 180f, animationSpec = tween(durationMillis = 300)
    )

    CustomIconButton(
        modifier = modifier.rotate(rotate),
        onClick = onClick,
        drawable = R.drawable.ic_expand_more,
        tint = LocalContentColor.current.copy(alpha = 0.6f)
    )
}