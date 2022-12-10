package com.leebeebeom.clothinghelper.ui.main.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.composable.SimpleIcon
import com.leebeebeom.clothinghelper.util.Anime
import kotlinx.coroutines.CoroutineScope

@Composable
fun BoxScope.ScrollToTopFab(show: () -> Boolean, toTop: (CoroutineScope) -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    AnimatedVisibility(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(4.dp),
        visible = show(),
        enter = Anime.ScrollToTopFab.scaleIn,
        exit = Anime.ScrollToTopFab.scaleOut
    ) {
        FloatingActionButton(
            onClick = { toTop(coroutineScope) },
            backgroundColor = MaterialTheme.colors.surface,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(36.dp),
            elevation = FloatingActionButtonDefaults.elevation(4.dp, 10.dp, 6.dp, 6.dp)
        ) { SimpleIcon(drawable = R.drawable.ic_expand_less) }
    }
}