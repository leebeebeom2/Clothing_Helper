package com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemState
import com.leebeebeom.clothinghelper.ui.drawer.OpenWhenNavigateToChild
import com.leebeebeom.clothinghelper.ui.drawer.OpenWhenNavigateToChildNoArg
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerExpandIcon
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerItemsWrapperWithExpandAnimation
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerRow
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerText
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.SubMenuType
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.SubMenus
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerCurrentPositionBackgroundColor
import com.leebeebeom.clothinghelper.ui.main.mainmenu.brand.BrandGraphRoute
import com.leebeebeom.clothinghelper.ui.main.mainmenu.clothes.ClothesGraphRoute
import com.leebeebeom.clothinghelper.ui.main.mainmenu.clothes.closet.ClosetGraphRoute
import com.leebeebeom.clothinghelper.ui.main.mainmenu.clothes.wish.WishGraphRoute
import com.leebeebeom.clothinghelper.ui.main.mainmenu.outfit.OutfitGraphRoute
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import com.leebeebeom.clothinghelper.ui.util.CurrentBackStack
import com.leebeebeom.clothinghelper.ui.util.DeleteFolder
import com.leebeebeom.clothinghelper.ui.util.EditFolder
import com.leebeebeom.clothinghelper.ui.util.FolderNames
import com.leebeebeom.clothinghelper.ui.util.Folders
import com.leebeebeom.clothinghelper.ui.util.FoldersSize
import com.leebeebeom.clothinghelper.ui.util.ItemsSize
import com.leebeebeom.clothinghelper.ui.util.OnClothesCategoryClick
import com.leebeebeom.clothinghelper.ui.util.OnFolderClick
import com.leebeebeom.clothinghelper.ui.util.OnMainMenuClick
import com.leebeebeom.clothinghelper.ui.util.OnSubMenuClick

const val DrawerMainMenuTag = "drawer main menu"

@Composable
fun DrawerMainMenu(
    mainMenu: MainMenu,
    onMainMenuClick: OnMainMenuClick,
    onSubMenuClick: OnSubMenuClick,
    onClothesCategoryClick: OnClothesCategoryClick,
    onFolderClick: OnFolderClick,
    folders: Folders,
    folderNames: FolderNames,
    foldersSize: FoldersSize,
    itemsSize: ItemsSize,
    addFolder: AddFolder,
    editFolder: EditFolder,
    deleteFolder: DeleteFolder,
    state: DrawerItemState,
    height: () -> Dp,
    currentBackStack: CurrentBackStack,
    toggleExpand: () -> Unit,
    expand: () -> Unit,
) {
    val route = when (mainMenu.type) {
        MainMenuType.Brand -> BrandGraphRoute.BrandMainScreen
        MainMenuType.Clothes -> ClothesGraphRoute.ClothesScreen
        MainMenuType.Outfit -> OutfitGraphRoute.OutfitScreen
        else -> throw IllegalStateException()
    }

    val currentPositionBackgroundColor by rememberDrawerCurrentPositionBackgroundColor(
        currentBackStack = currentBackStack,
        route = route
    )

    val childRoutes = when (mainMenu.type) {
        MainMenuType.Brand -> listOf(SubMenuType.Brand.name, SubMenuType.Shop.name)
        MainMenuType.Clothes -> listOf(ClosetGraphRoute.ClosetScreen, WishGraphRoute.WishScreen)
        MainMenuType.Outfit -> listOf(SubMenuType.Ootd.name, SubMenuType.Reference.name)
        else -> throw IllegalStateException()
    }

    childRoutes.forEach {
        if (mainMenu.type != MainMenuType.Clothes)
            OpenWhenNavigateToChild(
                currentBackStack = currentBackStack,
                childParentKey = it,
                expand = expand
            )
        else OpenWhenNavigateToChildNoArg(
            currentBackStack = currentBackStack,
            childRoute = it,
            expand = expand
        )
    }

    DrawerRow(
        modifier = Modifier.testTag(DrawerMainMenuTag),
        currentPositionBackgroundColor = { currentPositionBackgroundColor },
        onClick = { onMainMenuClick(mainMenu.type) },
        height = height
    ) {
        DrawerText(text = mainMenu.name, style = drawerMainMenuTextStyle())
        DrawerExpandIcon(expanded = { state.expanded }, toggleExpand = toggleExpand)
    }

    DrawerItemsWrapperWithExpandAnimation(expand = { state.expanded },
        item = {
            SubMenus(
                mainMenuType = mainMenu.type,
                onSubMenuClick = onSubMenuClick,
                onClothesCategoryClick = onClothesCategoryClick,
                foldersSize = foldersSize,
                folderNames = folderNames,
                itemsSize = itemsSize,
                addFolder = addFolder,
                folders = folders,
                height = height,
                currentBackStack = currentBackStack,
                onFolderClick = onFolderClick,
                deleteFolder = deleteFolder,
                editFolder = editFolder
            )
        })
}