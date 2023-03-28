package com.leebeebeom.clothinghelper.ui.drawer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.components.IconWrapper
import com.leebeebeom.clothinghelper.ui.components.SingleLineText
import com.leebeebeom.clothinghelper.ui.components.WidthSpacer
import com.leebeebeom.clothinghelper.ui.drawer.drawer.components.DrawerRow
import com.leebeebeom.clothinghelper.ui.theme.DarkGray
import com.leebeebeom.clothinghelper.ui.theme.Disabled
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

const val DrawerEssentialMenuHeight = 40
const val DrawerEssentialMenuIconSize = 22

@Composable
fun DrawerEssentialMenus(
    onEssentialMenuClick: (EssentialMenuType) -> Unit,
    essentialMenus: ImmutableList<EssentialMenu> = remember { getEssentialMenus() }
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = DarkGray),
        contentPadding = PaddingValues(top = 4.dp, bottom = 40.dp)
    ) {
        this.items(items = essentialMenus, key = { it.name }) { essentialMenu ->
            EssentialMenu(
                essentialMenu = essentialMenu,
                onClick = { essentialMenuType -> onEssentialMenuClick(essentialMenuType) },
            )
        }
        item { Divider(color = Disabled, modifier = Modifier.padding(vertical = 8.dp)) }
    }
}

@Composable
private fun EssentialMenu(
    essentialMenu: EssentialMenu, onClick: (EssentialMenuType) -> Unit
) {
    DrawerRow(
        modifier = Modifier.heightIn(DrawerEssentialMenuHeight.dp),
        onClick = { onClick(essentialMenu.type) }) {
        IconWrapper(
            modifier = Modifier.size(DrawerEssentialMenuIconSize.dp),
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