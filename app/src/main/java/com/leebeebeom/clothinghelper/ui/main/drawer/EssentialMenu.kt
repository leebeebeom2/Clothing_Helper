package com.leebeebeom.clothinghelper.ui.main.drawer

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.ui.components.IconWrapper
import com.leebeebeom.clothinghelper.ui.components.SingleLineText
import com.leebeebeom.clothinghelper.ui.components.WidthSpacer
import com.leebeebeom.clothinghelper.ui.main.drawer.components.DrawerRow
import com.leebeebeom.clothinghelper.ui.main.drawer.model.EssentialMenu
import com.leebeebeom.clothinghelper.ui.main.drawer.model.EssentialMenuType
import kotlinx.collections.immutable.ImmutableList

const val ESSENTIAL_MENU_HEIGHT = 40
const val ESSENTIAL_MENU_ICON_SIZE = 22

fun LazyListScope.essentialMenus(
    essentialMenus: ImmutableList<EssentialMenu>,
    onEssentialMenuClick: (essentialMenu: EssentialMenuType) -> Unit
) {
    items(items = essentialMenus, key = { it.name }) {
        EssentialMenu(
            essentialMenu = it,
            onClick = onEssentialMenuClick,
        )
    }
}

@Composable
private fun EssentialMenu(
    essentialMenu: EssentialMenu, onClick: (EssentialMenuType) -> Unit
) {
    DrawerRow(
        modifier = Modifier.heightIn(ESSENTIAL_MENU_HEIGHT.dp),
        onClick = { onClick(essentialMenu.type) }) {
        IconWrapper(
            modifier = Modifier.size(ESSENTIAL_MENU_ICON_SIZE.dp),
            drawable = essentialMenu.drawable
        )
        WidthSpacer(dp = 8)
        SingleLineText(
            text = stringResource(id = essentialMenu.name),
            style = MaterialTheme.typography.body1.copy(letterSpacing = 0.75.sp)
        )
    }
}