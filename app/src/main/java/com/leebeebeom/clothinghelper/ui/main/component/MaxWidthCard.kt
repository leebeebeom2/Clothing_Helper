package com.leebeebeom.clothinghelper.ui.main.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MaxWidthCard( // skippable
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = 2.dp, onClick = onClick) {
        content()
    }
}