package com.leebeebeom.clothinghelper.ui.drawer.component.mainmenu

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemState
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerExpandIcon
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerItemsWrapperWithExpandAnimation
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerRow
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerText

@Composable
fun DrawerMainMenu(
    mainMenu: MainMenu,
    onMainMenuClick: (MainMenuType) -> Unit,
    state: DrawerItemState,
    subMenus: @Composable (MainMenuType) -> Unit,
) {
    DrawerRow(onClick = { onMainMenuClick(mainMenu.type) }) {
        DrawerText(text = mainMenu.name, style = drawerMainMenuTextStyle())
        DrawerExpandIcon(expanded = { state.expanded }, toggleExpand = state::toggleExpand)
    }
    DrawerItemsWrapperWithExpandAnimation(expand = { state.expanded }) { subMenus(mainMenu.type) }
}