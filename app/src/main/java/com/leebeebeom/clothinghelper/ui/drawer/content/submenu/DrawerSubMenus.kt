package com.leebeebeom.clothinghelper.ui.drawer.content.submenu

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.MenuType
import com.leebeebeom.clothinghelper.ui.drawer.content.folder.getSubBackgroundColor
import com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu.MainMenuType
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.clothes.ClothesCategoryType
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.clothes.DrawerClothesSubMenu
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemState
import com.leebeebeom.clothinghelper.ui.theme.Black11
import com.leebeebeom.clothinghelper.ui.theme.Black18
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf

@Composable
fun SubMenus(
    mainMenuType: MainMenuType,
    onSubMenuClick: (SubMenuType) -> Unit,
    onClothesCategoryClick: (ClothesCategoryType) -> Unit,
    folderNames: (parentKey: String) -> ImmutableSet<String>,
    foldersSize: (parentKey: String) -> Int,
    itemsSize: (parentKey: String) -> Int,
    addFolder: AddFolder,
    height: () -> Dp,
    folders: @Composable (parentKey: String, backgroundColor: Color, basePadding: Dp) -> Unit
) {
    val subMenus = rememberSubMenus(mainMenuType = mainMenuType)

    Column(modifier = Modifier.background(Black11)) {
        subMenus.forEach { subMenu ->
            key(subMenu.type) {
                if (subMenu.type == SubMenuType.Brand || subMenu.type == SubMenuType.Shop ||
                    subMenu.type == SubMenuType.Ootd || subMenu.type == SubMenuType.Reference
                ) {
                    val state = rememberDrawerItemDropdownMenuState()

                    DrawerSubMenu(
                        state = state,
                        subMenu = subMenu,
                        onClick = onSubMenuClick,
                        folderNames = folderNames,
                        foldersSize = foldersSize,
                        itemsSize = itemsSize,
                        addFolder = addFolder,
                        folders = { parentKey, basePadding ->
                            folders(parentKey, Black18, basePadding)
                        },
                        height = height
                    )
                } else if (subMenu.type == SubMenuType.Closet || subMenu.type == SubMenuType.Wish) {
                    val state = rememberDrawerItemState()

                    DrawerClothesSubMenu(
                        state = state,
                        subMenu = subMenu,
                        onClick = onSubMenuClick,
                        onClothesCategoryClick = onClothesCategoryClick,
                        folderNames = folderNames,
                        foldersSize = foldersSize,
                        itemsSize = itemsSize,
                        addFolder = addFolder,
                        folders = { parentKey, basePadding ->
                            folders(parentKey, getSubBackgroundColor(Black18), basePadding)
                        },
                        height = height
                    )
                }
            }
        }
    }
}

data class SubMenu(
    @StringRes val name: Int, val type: SubMenuType
)

enum class SubMenuType {
    Brand, Shop, Closet, Wish, Ootd, Reference
}

fun SubMenuType.toMenuType() = when (this) {
    SubMenuType.Brand -> MenuType.Brand
    SubMenuType.Shop -> MenuType.Shop
    SubMenuType.Ootd -> MenuType.Ootd
    SubMenuType.Reference -> MenuType.Reference
    else -> throw IllegalStateException()
}

@Composable
fun rememberSubMenus(mainMenuType: MainMenuType) = remember(mainMenuType) {
    when (mainMenuType) {
        MainMenuType.Brand -> getBrandSubMenus()
        MainMenuType.Clothes -> getClotheSubMenus()
        MainMenuType.Outfit -> getOutfitSubMenus()
        MainMenuType.Archive -> persistentListOf()
    }
}

fun getBrandSubMenus() = persistentListOf(
    SubMenu(R.string.brand_cap, SubMenuType.Brand),
    SubMenu(R.string.shop_cap, SubMenuType.Shop),
)

fun getClotheSubMenus() = persistentListOf(
    SubMenu(R.string.closet_cap, SubMenuType.Closet),
    SubMenu(R.string.wish_cap, SubMenuType.Wish),
)

fun getOutfitSubMenus() = persistentListOf(
    SubMenu(R.string.ootd_cap, SubMenuType.Ootd),
    SubMenu(R.string.reference_cap, SubMenuType.Reference),
)