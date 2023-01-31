package com.leebeebeom.clothinghelper.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.ui.theme.Disabled

const val CENTER_DOT_PROGRESS_INDICATOR = "center dot progress indicator"
const val DOT_PROGRESS_INDICATOR = "dot progress indicator"

@Composable
fun CenterDotProgressIndicator(
    backGround: Color = Disabled, show: () -> Boolean
) {
    if (show()) Surface(color = backGround) {
        Box(modifier = Modifier
            .testTag(CENTER_DOT_PROGRESS_INDICATOR)
            .fillMaxSize()
            // background click block
            .clickable(enabled = false) { }) {

            DotProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                size = 8.dp,
                color = LocalContentColor.current.copy(ContentAlpha.medium)
            )
        }
    }
}

@Composable
fun DotProgressIndicator(
    modifier: Modifier,
    size: Dp,
    color: Color = MaterialTheme.colors.primary.copy(ContentAlpha.disabled)
) {
    val maxOffset = 4f
    val delayUnit = 300

    @Composable
    fun Dot(offset: Float) = Spacer(
        Modifier
            .size(size)
            .offset(y = -offset.dp)
            .background(
                color = color, shape = CircleShape
            )
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
        modifier = modifier.padding(top = maxOffset.dp).testTag(DOT_PROGRESS_INDICATOR)
    ) {
        Dot(offset1)
        Spacer(Modifier.width(4.dp))
        Dot(offset2)
        Spacer(Modifier.width(4.dp))
        Dot(offset3)
    }
}