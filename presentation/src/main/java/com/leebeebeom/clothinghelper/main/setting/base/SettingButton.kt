package com.leebeebeom.clothinghelper.main.setting.base

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.SimpleIcon
import com.leebeebeom.clothinghelper.base.composables.SingleLineText

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingButton(@StringRes text: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SingleLineText(
                modifier = Modifier.weight(1f),
                text = text,
                style = MaterialTheme.typography.body1.copy(fontSize = 17.sp, color = LocalContentColor.current.copy(0.8f))
            )
            SimpleIcon(
                drawable = R.drawable.ic_navigate_next,
                tint = LocalContentColor.current.copy(0.6f)
            )
        }
    }
}