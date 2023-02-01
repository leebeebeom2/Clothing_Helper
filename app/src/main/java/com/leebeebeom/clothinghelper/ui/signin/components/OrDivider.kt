package com.leebeebeom.clothinghelper.ui.signin.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.components.HeightSpacer
import com.leebeebeom.clothinghelper.ui.components.SingleLineText

@Composable
fun OrDivider() {
    HeightSpacer(dp = 12)
    Row(verticalAlignment = Alignment.CenterVertically) {
        val weightModifier = Modifier.weight(1f)

        Divider(modifier = weightModifier)
        SingleLineText(
            text = R.string.or,
            modifier = Modifier.padding(horizontal = 12.dp),
            style = MaterialTheme.typography.body2.copy(
                color = LocalContentColor.current.copy(ContentAlpha.disabled),
                fontWeight = FontWeight.Bold
            )
        )
        Divider(modifier = weightModifier)
    }
    HeightSpacer(dp = 12)
}