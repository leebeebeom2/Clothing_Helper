package com.leebeebeom.clothinghelper.ui.main.essentialmenu.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.component.DoubleCountText
import com.leebeebeom.clothinghelper.ui.component.HeightSpacer
import com.leebeebeom.clothinghelper.ui.component.IconWrapper
import com.leebeebeom.clothinghelper.ui.component.SingleLineText
import com.leebeebeom.clothinghelper.ui.component.WidthSpacer
import com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu.MainMenuType
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.SubMenu
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.SubMenuType
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.rememberSubMenus
import com.leebeebeom.clothinghelper.ui.util.Anime

@Composable
fun MainScreenSubmenus(
    isExpand: () -> Boolean,
    mainMenuType: MainMenuType,
    onSubMenuClick: (SubMenuType) -> Unit,
    foldersSize: (String) -> Int
) {
    val subMenus = rememberSubMenus(mainMenuType = mainMenuType)
    val localIsExpand by remember { derivedStateOf(isExpand) }

    AnimatedVisibility(
        visible = localIsExpand,
        enter = Anime.MainScreen.expand,
        exit = Anime.MainScreen.shrink
    ) {
        Divider(modifier = Modifier.fillMaxWidth())
        Column(
            Modifier.padding(vertical = 6.dp)
        ) {
            subMenus.forEach {
                MainScreenSubMenu(
                    subMenu = it,
                    onClick = onSubMenuClick,
                    foldersSize = foldersSize
                )
            }
        }
    }
}

@Composable
private fun MainScreenSubMenu(
    subMenu: SubMenu,
    onClick: (SubMenuType) -> Unit,
    foldersSize: (String) -> Int
) {
    val localFoldersSize by remember(foldersSize) { derivedStateOf { foldersSize(subMenu.type.name) } }

    Row(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clip(MaterialTheme.shapes.small)
            .clickable { onClick(subMenu.type) }
            .fillMaxWidth()
            .padding(
                vertical =
                if (subMenu.type != SubMenuType.Closet && subMenu.type != SubMenuType.Wish) 6.dp else 12.dp
            )
            .padding(start = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconWrapper(drawable = R.drawable.ic_dot)
        WidthSpacer(dp = 8)
        Column(Modifier.weight(1f)) {
            SingleLineText(
                text = subMenu.name,
                style = MaterialTheme.typography.h6.copy(fontSize = 18.sp)
            )
            if (subMenu.type != SubMenuType.Closet && subMenu.type != SubMenuType.Wish) {
                HeightSpacer(dp = 2)
                DoubleCountText(foldersSize = { localFoldersSize }, itemsSize = { 0 })
            }
        }
    }
}