package com.leebeebeom.clothinghelper.main.base

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.CustomIconButton

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun AllExpandIcon(
    size: Dp,
    allExpandIconClick: () -> Unit,
    tint: Color,
    allExpand: Boolean,
    rippleSize: Dp = 4.dp
) {
    val painter = rememberAnimatedVectorPainter(
        animatedImageVector = AnimatedImageVector.animatedVectorResource(
            id = R.drawable.all_expand_anim
        ), atEnd = allExpand
    )

    CustomIconButton(
        modifier = Modifier.size(size),
        onClick = allExpandIconClick,
        painter = painter,
        tint = tint,
        rippleSize = rippleSize
    )
}

abstract class AllExpandStateHolder {
    abstract val isAllExpand: Boolean
    protected abstract val _isExpandState: MutableState<Boolean>
    val isExpandState get() = _isExpandState.value

    fun isExpandToggle() {
        _isExpandState.value = !_isExpandState.value
    }
}

@Composable
fun ExpandIcon(modifier: Modifier = Modifier, isExpanded: Boolean, onExpandIconClick: () -> Unit) {
    val rotate by animateFloatAsState(
        targetValue = if (!isExpanded) 0f else 180f, animationSpec = tween(durationMillis = 300)
    )

    CustomIconButton(
        modifier = modifier.rotate(rotate),
        onClick = onExpandIconClick,
        drawable = R.drawable.ic_expand_more,
        tint = LocalContentColor.current.copy(alpha = 0.6f)
    )
}