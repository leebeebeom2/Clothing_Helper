package com.leebeebeom.clothinghelper.main.setting.base

import androidx.annotation.StringRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.base.composables.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.composables.SingleLineText

@Composable
fun SettingTitle(@StringRes title: Int, @StringRes subTitle: Int? = null) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxSize(),
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

    SimpleHeightSpacer(dp = 8)
    Divider(modifier = Modifier.fillMaxWidth())
    SimpleHeightSpacer(dp = 8)
}