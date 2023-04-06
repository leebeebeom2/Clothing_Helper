package com.leebeebeom.clothinghelper.ui.drawer.component.mainmenu

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.ui.component.SingleLineText
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemState
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerExpandIcon
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerInsideRow
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerItemsWrapperWithExpandAnimation
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerRow
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemState

@Composable
fun drawerMainMenuTextStyle() = MaterialTheme.typography.h6

@Composable
fun DrawerMainMenu(
    mainMenu: MainMenu,
    onMainMenuClick: (MainMenuType) -> Unit,
    state: DrawerItemState = rememberDrawerItemState(),
    subMenus: @Composable (MainMenuType) -> Unit,
) {
    DrawerRow(onClick = { onMainMenuClick(mainMenu.type) }) {
        DrawerInsideRow { SingleLineText(text = mainMenu.name, style = drawerMainMenuTextStyle()) }
        DrawerExpandIcon(expanded = { state.expanded }, toggleExpand = state::toggleExpand)
    }
    DrawerItemsWrapperWithExpandAnimation(expand = { state.expanded }) { subMenus(mainMenu.type) }
}