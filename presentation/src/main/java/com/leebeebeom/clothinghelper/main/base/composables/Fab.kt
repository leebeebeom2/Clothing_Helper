package com.leebeebeom.clothinghelper.main.base.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.Anime

const val fabEndPadding = 20
const val fabBottomPadding = 24

@Composable
fun BoxScope.Fab(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(
        end = fabEndPadding.dp,
        bottom = fabBottomPadding.dp
    ),
    align: Alignment = Alignment.BottomEnd,
    size: Dp = 48.dp,
    visible: () -> Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible(),
        modifier = Modifier.align(align),
        enter = Anime.SelectModeFabFade.fadeIn,
        exit = Anime.SelectModeFabFade.fadeOut
    ) {
        FloatingActionButton(
            modifier = modifier
                .padding(paddingValues)
                .size(size),
            onClick = onClick,
            backgroundColor = MaterialTheme.colors.primary,
            content = content
        )
    }
}