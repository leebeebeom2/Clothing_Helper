package com.leebeebeom.clothinghelper.main.base.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.base.Anime

@Composable
fun BoxScope.Fab(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(end = 24.dp, bottom = 32.dp),
    align: Alignment = Alignment.BottomEnd,
    size: Dp = 48.dp,
    visible: () -> Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible(),
        modifier = Modifier.align(align),
        enter = Anime.AddSubCategoryFab.fadeIn,
        exit = Anime.AddSubCategoryFab.fadeOut
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