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
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.components.IconWrapper
import com.leebeebeom.clothinghelper.ui.components.SingleLineText
import com.leebeebeom.clothinghelper.ui.components.WidthSpacer
import com.leebeebeom.clothinghelper.ui.main.drawer.components.DrawerRow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

const val DRAWER_ESSENTIAL_MENU_HEIGHT = 40
const val DRAWER_ESSENTIAL_MENU_ICON_SIZE = 22

fun LazyListScope.drawerEssentialMenus(
    essentialMenus: ImmutableList<EssentialMenu>,
    onEssentialMenuClick: (essentialMenu: EssentialMenuType) -> Unit
) {
    items(items = essentialMenus, key = { it.name }) {
        DrawerEssentialMenu(
            essentialMenu = it,
            onClick = onEssentialMenuClick,
        )
    }
}

@Composable
private fun DrawerEssentialMenu(
    essentialMenu: EssentialMenu, onClick: (EssentialMenuType) -> Unit
) {
    DrawerRow(
        modifier = Modifier.heightIn(DRAWER_ESSENTIAL_MENU_HEIGHT.dp),
        onClick = { onClick(essentialMenu.type) }) {
        IconWrapper(
            modifier = Modifier.size(DRAWER_ESSENTIAL_MENU_ICON_SIZE.dp),
            drawable = essentialMenu.drawable
        )
        WidthSpacer(dp = 8)
        SingleLineText(
            text = stringResource(id = essentialMenu.name),
            style = MaterialTheme.typography.body1.copy(letterSpacing = 0.75.sp)
        )
    }
}

data class EssentialMenu(
    val name: Int, val drawable: Int, val type: EssentialMenuType
)

enum class EssentialMenuType {
    MainScreen, Favorite, SeeAll, Trash
}

fun getEssentialMenus() =
    listOf(
        EssentialMenu(R.string.main_screen, R.drawable.ic_home, EssentialMenuType.MainScreen),
        EssentialMenu(R.string.favorite, R.drawable.ic_star, EssentialMenuType.Favorite),
        EssentialMenu(R.string.see_all, R.drawable.ic_list, EssentialMenuType.SeeAll),
        EssentialMenu(R.string.trash, R.drawable.ic_delete, EssentialMenuType.Trash)
    ).toImmutableList()