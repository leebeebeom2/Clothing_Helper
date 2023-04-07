package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.leebeebeom.clothinghelper.ui.util.Anime

@Composable
fun DrawerItemsWrapperWithExpandAnimation(
    expand: () -> Boolean, item: @Composable () -> Unit, draw: () -> Boolean = { true }
) {
    val localDraw by remember(draw) { derivedStateOf(draw) }
    val localExpand by remember(expand) { derivedStateOf(expand) }

    if (localDraw) AnimatedVisibility(
        visible = localExpand,
        enter = Anime.DrawerList.listExpand,
        exit = Anime.DrawerList.listShrink
    ) {
        item()
    }
}