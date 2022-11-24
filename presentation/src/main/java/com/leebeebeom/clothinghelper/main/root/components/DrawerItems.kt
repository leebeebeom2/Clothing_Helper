package com.leebeebeom.clothinghelper.main.root.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import com.leebeebeom.clothinghelper.base.Anime
import com.leebeebeom.clothinghelper.theme.DarkGray
import com.leebeebeom.clothinghelperdomain.model.container.BaseContainer
import kotlinx.collections.immutable.ImmutableList

@Composable
fun <T : BaseContainer> DrawerItems(
    show: () -> Boolean,
    items: () -> ImmutableList<T>,
    depth: Int,
    item: @Composable (T) -> Unit,
) {
    AnimatedVisibility(
        visible = show(),
        enter = Anime.DrawerList.listExpand,
        exit = Anime.DrawerList.listShrink
    ) {
        key("drawerItems") {
            Surface(color = if (depth % 2 == 0) DarkGray else MaterialTheme.colors.primary) {
                Column { items().forEach { key(it.key) { item(it) } } }
            }
        }
    }
}