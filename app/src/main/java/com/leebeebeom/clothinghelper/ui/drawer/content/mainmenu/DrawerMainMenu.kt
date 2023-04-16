package com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemState
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerExpandIcon
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerItemsWrapperWithExpandAnimation
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerRow
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerText

const val DrawerMainMenuTag = "drawer main menu"

@Composable
fun DrawerMainMenu(
    mainMenu: MainMenu,
    onMainMenuClick: (MainMenuType) -> Unit,
    state: DrawerItemState,
    subMenus: @Composable (MainMenuType) -> Unit,
) {
    DrawerRow(
        modifier = Modifier.testTag(DrawerMainMenuTag),
        onClick = { onMainMenuClick(mainMenu.type) }) {
        DrawerText(text = mainMenu.name, style = drawerMainMenuTextStyle())
        DrawerExpandIcon(expanded = { state.expanded }, toggleExpand = state::toggleExpand)
    }

    DrawerItemsWrapperWithExpandAnimation(expand = { state.expanded },
        item = { subMenus(mainMenu.type) })
}