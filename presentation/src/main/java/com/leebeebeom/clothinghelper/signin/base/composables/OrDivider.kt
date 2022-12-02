package com.leebeebeom.clothinghelper.signin.base.composables

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
import com.leebeebeom.clothinghelper.base.composables.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.composables.SingleLineText

@Composable
fun OrDivider() {
    SimpleHeightSpacer(dp = 12)
    Row(verticalAlignment = Alignment.CenterVertically) {
        val modifier = Modifier.weight(1f)

        Divider(modifier = modifier)
        SingleLineText(
            text = R.string.or,
            modifier = Modifier.padding(horizontal = 14.dp),
            style = MaterialTheme.typography.body2.copy(
                color = LocalContentColor.current.copy(ContentAlpha.disabled),
                fontWeight = FontWeight.Bold
            )
        )
        Divider(modifier = modifier)
    }
    SimpleHeightSpacer(dp = 12)
}