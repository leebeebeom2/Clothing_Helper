package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.leebeebeom.clothinghelper.ui.util.Anime

@Composable
fun DrawerItemsWrapperWithExpandAnimation(
    expand: () -> Boolean,
    item: @Composable () -> Unit,
) {
    val localExpand by remember(expand) { derivedStateOf(expand) }

    AnimatedVisibility(
        visible = localExpand,
        enter = Anime.DrawerList.listExpand,
        exit = Anime.DrawerList.listShrink
    ) {
        item()
    }
}