package com.leebeebeom.clothinghelper.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.ui.theme.Disabled

const val CenterDotProgressIndicatorTag = "center dot progress indicator"

@Composable
fun CenterDotProgressIndicator(
    backGround: Color = Disabled,
    show: () -> Boolean,
) {
    val localShow by remember(show) { derivedStateOf(show) }

    if (localShow) Surface(color = backGround) {
        Box(modifier = Modifier
            .fillMaxSize()
            // background click block
            .clickable(enabled = false) { }
            .testTag(CenterDotProgressIndicatorTag),
            contentAlignment = Alignment.Center
        ) {
            DotProgressIndicator(
                size = 8.dp,
                color = LocalContentColor.current.copy(ContentAlpha.medium)
            )
        }
        BackHandlerWrapper(enabled = { true }, task = {})
    }
}

@Composable
fun DotProgressIndicator(
    size: Dp,
    color: Color,
) {
    val maxOffset = 4f
    val delayUnit = 300

    @Composable
    fun Dot(offset: Float) = Spacer(
        Modifier
            .size(size = size)
            .offset(y = -offset.dp)
            .background(color = color, shape = CircleShape)
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateOffsetWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(animation = keyframes {
            durationMillis = delayUnit * 4
            0f at delay with LinearEasing
            maxOffset at delay + delayUnit with LinearEasing
            0f at delay + delayUnit * 2
        })
    )

    val offset1 by animateOffsetWithDelay(0)
    val offset2 by animateOffsetWithDelay(delayUnit)
    val offset3 by animateOffsetWithDelay(delayUnit * 2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = maxOffset.dp)
    ) {
        Dot(offset = offset1)
        Spacer(modifier = Modifier.width(4.dp))
        Dot(offset = offset2)
        Spacer(modifier = Modifier.width(4.dp))
        Dot(offset = offset3)
    }
}