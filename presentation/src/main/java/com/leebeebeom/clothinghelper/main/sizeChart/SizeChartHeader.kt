package com.leebeebeom.clothinghelper.main.sizeChart

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.base.composables.SingleLineText

@Composable
fun SizeChartHeader(path: String) {
    Row(modifier = Modifier
        .horizontalScroll(rememberScrollState())
        .padding(vertical = 8.dp, horizontal = 12.dp)) {
        SingleLineText(text = path, style = MaterialTheme.typography.h4.copy(fontSize = 24.sp))
    }

    Divider(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp))
}