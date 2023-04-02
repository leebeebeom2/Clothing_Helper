package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.ui.util.Anime

@Composable
fun DrawerItemsWrapperWithExpandAnimation(
    expand: () -> Boolean,
    item: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = expand(),
        enter = Anime.DrawerList.listExpand,
        exit = Anime.DrawerList.listShrink
    ) {
        item()
    }
}