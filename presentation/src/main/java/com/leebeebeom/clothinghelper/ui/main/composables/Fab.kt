package com.leebeebeom.clothinghelper.ui.main.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.util.Anime

@Composable
fun BoxScope.Fab(
    visible: () -> Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible(),
        modifier = Modifier.align(Alignment.BottomEnd),
        enter = Anime.SelectModeFabFade.fadeIn,
        exit = Anime.SelectModeFabFade.fadeOut
    ) {
        FloatingActionButton(
            modifier = Modifier
                .padding(end = 20.dp, bottom = 24.dp)
                .size(48.dp),
            onClick = onClick,
            backgroundColor = MaterialTheme.colors.primary,
            content = content
        )
    }
}