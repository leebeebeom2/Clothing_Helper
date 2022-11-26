package com.leebeebeom.clothinghelper.main.detail.contents

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.base.composables.SingleLineText

@Composable
fun DetailHeader(title: String) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.horizontalScroll(rememberScrollState()),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SingleLineText(
                text = title,
                style = MaterialTheme.typography.h2.copy(
                    color = Color.Black.copy(alpha = 0.8f),
                    fontSize = 28.sp
                )
            )
        }
    }
}