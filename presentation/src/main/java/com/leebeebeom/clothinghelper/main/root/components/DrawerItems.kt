package com.leebeebeom.clothinghelper.main.root.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.graphics.Color
import com.leebeebeom.clothinghelper.base.Anime
import com.leebeebeom.clothinghelperdomain.model.container.BaseContainer
import kotlinx.collections.immutable.ImmutableList

@Composable
fun <T : BaseContainer> DrawerItems(
    show: () -> Boolean,
    items: () -> ImmutableList<T>,
    backGround: Color,
    item: @Composable (T) -> Unit,
) {
    AnimatedVisibility(
        visible = show(),
        enter = Anime.DrawerList.listExpand,
        exit = Anime.DrawerList.listShrink
    ) {
        Surface(color = backGround) {
            Column { items().forEach { key(it.key) { item(it) } } }
        }
    }
}