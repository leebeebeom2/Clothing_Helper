package com.leebeebeom.clothinghelper.main.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.Anime
import com.leebeebeom.clothinghelper.base.CustomIconButton

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun AllExpandIcon(
    size: Dp,
    onClick: () -> Unit,
    tint: Color,
    isAllExpanded: () -> Boolean
) {
    val painter = rememberAnimatedVectorPainter(
        animatedImageVector = AnimatedImageVector.animatedVectorResource(
            id = R.drawable.all_expanded_anim
        ), atEnd = isAllExpanded()
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
    isAllExpanded: () -> Boolean,
    updateIsExpand: (Boolean) -> Unit
) {
    val isAllExpandVal = isAllExpanded()
    var rememberedIsAllExpand by rememberSaveable {
        updateIsExpand(isAllExpandVal)
        mutableStateOf(isAllExpandVal)
    }
    if (isAllExpandVal != rememberedIsAllExpand) {
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

@Composable
fun BoxScope.Fab(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(end = 24.dp, bottom = 32.dp),
    align: Alignment = Alignment.BottomEnd,
    size: Dp = 48.dp,
    visible: () -> Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible(),
        modifier = Modifier.align(align),
        enter = Anime.AddSubCategoryFab.fadeIn,
        exit = Anime.AddSubCategoryFab.fadeOut
    ) {
        FloatingActionButton(
            modifier = modifier
                .padding(paddingValues)
                .size(size),
            onClick = onClick,
            backgroundColor = MaterialTheme.colors.primary,
            content = content
        )
    }
}