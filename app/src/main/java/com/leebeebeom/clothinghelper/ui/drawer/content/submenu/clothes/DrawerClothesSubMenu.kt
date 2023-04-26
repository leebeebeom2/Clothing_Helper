package com.leebeebeom.clothinghelper.ui.drawer.content.submenu.clothes

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.MenuType
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemState
import com.leebeebeom.clothinghelper.ui.drawer.OpenWhenNavigateToChild
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerDotIcon
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerExpandIcon
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerItemsWrapperWithExpandAnimation
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerRow
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerText
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.DrawerSubMenuTag
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.SubMenu
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.SubMenuType
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerCurrentPositionBackgroundColor
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.main.mainmenu.clothes.closet.ClosetGraphRoute
import com.leebeebeom.clothinghelper.ui.main.mainmenu.clothes.wish.WishGraphRoute
import com.leebeebeom.clothinghelper.ui.theme.Black18
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
import com.leebeebeom.clothinghelper.ui.util.OnSubMenuClick
import kotlinx.collections.immutable.persistentListOf

@Composable
fun DrawerClothesSubMenu(
    subMenu: SubMenu,
    onSubMenuClick: OnSubMenuClick,
    onFolderClick: OnFolderClick,
    onClothesCategoryClick: OnClothesCategoryClick,
    folders: Folders,
    folderNames: FolderNames,
    foldersSize: FoldersSize,
    itemsSize: ItemsSize,
    addFolder: AddFolder,
    editFolder: EditFolder,
    deleteFolder: DeleteFolder,
    state: DrawerItemState,
    height: () -> Dp,
    toggleExpand: () -> Unit,
    currentBackStack: CurrentBackStack,
    expand: () -> Unit
) {
    val route = remember(subMenu) {
        when (subMenu.type) {
            SubMenuType.Closet -> ClosetGraphRoute.ClosetScreen
            SubMenuType.Wish -> WishGraphRoute.WishScreen
            else -> throw IllegalStateException()
        }
    }
    val backgroundColor by rememberDrawerCurrentPositionBackgroundColor(
        currentBackStack = currentBackStack,
        route = route
    )

    val childParentKeys = remember(subMenu.type) {
        when (subMenu.type) {
            SubMenuType.Closet -> listOf(
                ClothesCategoryType.Closet.Top,
                ClothesCategoryType.Closet.Bottom,
                ClothesCategoryType.Closet.Outer,
                ClothesCategoryType.Closet.Etc,
            )

            SubMenuType.Wish -> listOf(
                ClothesCategoryType.Wish.Top,
                ClothesCategoryType.Wish.Bottom,
                ClothesCategoryType.Wish.Outer,
                ClothesCategoryType.Wish.Etc,
            )

            else -> throw IllegalStateException()
        }
    }

    childParentKeys.forEach {
        OpenWhenNavigateToChild(
            currentBackStack = currentBackStack,
            childParentKey = it.name,
            expand = expand
        )
    }

    val clothesCategories = rememberClosetCategories(subMenuType = subMenu.type)

    DrawerRow(
        Modifier
            .padding(start = 8.dp)
            .testTag(DrawerSubMenuTag),
        currentPositionBackgroundColor = { backgroundColor },
        onClick = { onSubMenuClick(subMenu.type) }, height = height
    ) {
        DrawerDotIcon()
        DrawerText(
            text = subMenu.name, style = MaterialTheme.typography.subtitle1.copy(fontSize = 18.sp)
        )
        DrawerExpandIcon(expanded = { state.expanded }, toggleExpand = toggleExpand)
    }

    DrawerItemsWrapperWithExpandAnimation(expand = { state.expanded }, item = {
        Column(modifier = Modifier.background(Black18)) {
            clothesCategories.forEach { clothesCategory ->
                key(clothesCategory.type) {
                    val subState = rememberDrawerItemDropdownMenuState()

                    DrawerClothesCategory(
                        state = subState,
                        clothesCategory = clothesCategory,
                        onClick = onClothesCategoryClick,
                        foldersSize = foldersSize,
                        folderNames = folderNames,
                        itemsSize = itemsSize,
                        addFolder = addFolder,
                        folders = folders,
                        height = height,
                        onLongClick = subState::onLongClick,
                        onSizeChanged = subState::onSizeChanged,
                        toggleExpand = subState::toggleExpand,
                        onDismissDropdownMenu = subState::onDismissDropdownMenu,
                        expand = subState::expand,
                        currentBackStack = currentBackStack,
                        onFolderClick = onFolderClick,
                        editFolder = editFolder,
                        deleteFolder = deleteFolder
                    )
                }
            }
        }
    })
}

sealed class ClothesCategoryType(val name: String) {
    sealed class Closet(name: String) : ClothesCategoryType(name) {
        object Top : Closet("closet top")
        object Bottom : Closet("closet bottom")
        object Outer : Closet("closet outer")
        object Etc : Closet("closet etc")
    }

    sealed class Wish(name: String) : ClothesCategoryType(name) {
        object Top : Wish("wish top")
        object Bottom : Wish("wish bottom")
        object Outer : Wish("wish outer")
        object Etc : Wish("wish etc")
    }
}

fun ClothesCategoryType.toMenuType() = when (this) {
    is ClothesCategoryType.Closet -> MenuType.ClosetDetail
    is ClothesCategoryType.Wish -> MenuType.WishDetail
}

data class ClothesCategory(
    @StringRes val name: Int, val type: ClothesCategoryType
)

@Composable
fun rememberClosetCategories(subMenuType: SubMenuType) = remember {
    when (subMenuType) {
        SubMenuType.Closet -> persistentListOf(
            ClothesCategory(R.string.tops_cap, ClothesCategoryType.Closet.Top),
            ClothesCategory(R.string.bottoms_cap, ClothesCategoryType.Closet.Bottom),
            ClothesCategory(R.string.outers_cap, ClothesCategoryType.Closet.Outer),
            ClothesCategory(R.string.etc_cap, ClothesCategoryType.Closet.Etc),
        )

        SubMenuType.Wish -> persistentListOf(
            ClothesCategory(R.string.tops_cap, ClothesCategoryType.Wish.Top),
            ClothesCategory(R.string.bottoms_cap, ClothesCategoryType.Wish.Bottom),
            ClothesCategory(R.string.outers_cap, ClothesCategoryType.Wish.Outer),
            ClothesCategory(R.string.etc_cap, ClothesCategoryType.Wish.Etc),
        )

        else -> throw IllegalStateException()
    }
}