package com.leebeebeom.clothinghelper.main.root.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.graphics.Color
import com.leebeebeom.clothinghelper.base.Anime
import com.leebeebeom.clothinghelperdomain.model.data.BaseModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun <T : BaseModel> DrawerItems(
    show: () -> Boolean,
    items: () -> ImmutableList<T>,
    background: Color,
    item: @Composable (T) -> Unit,
) {
    AnimatedVisibility(
        visible = show(),
        enter = Anime.DrawerList.listExpand,
        exit = Anime.DrawerList.listShrink
    ) {
        Surface(color = background) {
            Column { items().forEach { key(it.key) { item(it) } } }
        }
    }
}