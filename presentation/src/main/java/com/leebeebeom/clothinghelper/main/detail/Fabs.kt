package com.leebeebeom.clothinghelper.main.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelper.main.base.Fab

@Composable
fun BoxScope.AddFab() {
    var isClicked by remember { mutableStateOf(false) }

    Box(modifier = Modifier.align(Alignment.BottomEnd)) {
        AddFolderFab(isClicked = { isClicked })
        AddContentFab(isClicked = { isClicked })
        Fab(
            visible = { true },
            onClick = { isClicked = !isClicked },
            paddingValues = PaddingValues(),
            align = Alignment.Center
        ) {
            val rotate by animateFloatAsState(targetValue = if (isClicked) 136f else 0f)
            SimpleIcon(drawable = R.drawable.ic_add, modifier = Modifier.rotate(rotate))
        }
    }
}

@Composable
private fun BoxScope.AddFolderFab(isClicked: () -> Boolean) {
    val offsetY by animateIntAsState(targetValue = if (isClicked()) -260 else 0)
    Fab(
        size = 36.dp,
        paddingValues = PaddingValues(),
        align = Alignment.Center,
        visible = { true },
        onClick = { /*TODO*/ },
        modifier = Modifier.offset { IntOffset(x = 0, y = offsetY) },
    ) { SimpleIcon(drawable = R.drawable.ic_create_new_folder) }
}

@Composable
private fun BoxScope.AddContentFab(isClicked: () -> Boolean) {
    val offsetY by animateIntAsState(targetValue = if (isClicked()) -136 else 0)
    Fab(
        size = 36.dp,
        paddingValues = PaddingValues(),
        align = Alignment.Center,
        visible = { true },
        onClick = { /*TODO*/ },
        modifier = Modifier.offset { IntOffset(x = 0, y = offsetY) },
    ) { SimpleIcon(drawable = R.drawable.ic_add_content) }
}