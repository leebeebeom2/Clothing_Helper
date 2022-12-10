package com.leebeebeom.clothinghelper.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun TitleWithDivider(
    @StringRes title: Int,
    @StringRes subTitle: Int? = null,
    icon: @Composable (() -> Unit)? = null
) {
    Column {
        TitleWithHorizontalScroll(title = title, subTitle = subTitle)
        if (icon == null) SimpleHeightSpacer(dp = 8)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Divider(modifier = Modifier.weight(1f))
            icon?.invoke()
        }
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
                text = title,
                style = MaterialTheme.typography.h5.copy(fontSize = 28.sp)
            )

            subTitle?.let {
                SingleLineText(
                    text = it,
                    style = MaterialTheme.typography.body2.copy(
                        LocalContentColor.current.copy(ContentAlpha.medium)
                    )
                )
            }
        }
    }
}