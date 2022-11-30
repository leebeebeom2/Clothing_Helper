package com.leebeebeom.clothinghelper.main.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.composables.SimpleHeightSpacer

@Composable
fun FrontLayerContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DragHandle()
        SimpleHeightSpacer(dp = 52)
        Divider()
    }
}

@Composable
fun DragHandle() {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .height(4.dp)
            .width(40.dp)
            .background(Color(0xffcccccc))
    )
}