package com.leebeebeom.clothinghelper.ui.drawer.contents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.ui.theme.DarkGray

@Composable // skippable
fun DrawerContents(
    content: (LazyListScope).() -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = DarkGray),
        contentPadding = PaddingValues(top = 4.dp, bottom = 40.dp)
    ) {
        content()
    }
}