package com.leebeebeom.clothinghelper.ui.drawer.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

const val DrawerTag = "drawer"

@Composable // skippable
fun DrawerMenus(content: @Composable () -> Unit) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .testTag(DrawerTag)
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(bottom = 40.dp)
    ) {
        content()
    }
}