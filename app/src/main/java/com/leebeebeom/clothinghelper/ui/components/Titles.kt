package com.leebeebeom.clothinghelper.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val SPACE = 8
const val DIVIDER_Y_OFFSET = -40

@Composable
fun TitleWithDivider(
    @StringRes title: Int, @StringRes subTitle: Int? = null, icon: @Composable (() -> Unit)? = null
) {
    Column(modifier = Modifier.padding(start = 16.dp)) {
        HeightSpacer(dp = SPACE)
        TitleWithHorizontalScroll(title = title, subTitle = subTitle)
        DividerWithIcon(icon)
    }
}

@Composable
fun DividerWithIcon(icon: @Composable (() -> Unit)? = null) {
    Row(
        modifier = Modifier.offset { IntOffset(0, icon?.let { DIVIDER_Y_OFFSET } ?: 0) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon == null) HeightSpacer(dp = SPACE)
        Divider(modifier = Modifier.weight(1f))
        icon?.invoke()
    }
}

@Composable
fun TitleWithHorizontalScroll(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    @StringRes subTitle: Int? = null
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            SingleLineText(
                text = title, style = MaterialTheme.typography.h4.copy(fontSize = 30.sp)
            )

            subTitle?.let {
                SingleLineText(
                    text = it, style = MaterialTheme.typography.body2.copy(
                        LocalContentColor.current.copy(ContentAlpha.medium)
                    )
                )
            }
        }
    }
}