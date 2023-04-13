package com.leebeebeom.clothinghelper.ui.drawer.content.submenu.clothes

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemState
import com.leebeebeom.clothinghelper.ui.drawer.component.*
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.SubMenu
import com.leebeebeom.clothinghelper.ui.drawer.content.submenu.SubMenuType
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.theme.Black18
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf

@Composable
fun DrawerClothesSubMenu(
    subMenu: SubMenu,
    onClick: (SubMenuType) -> Unit,
    onClothesCategoryClick: (ClothesCategoryType) -> Unit,
    folderNames: (parentKey: String) -> ImmutableSet<String>,
    foldersSize: (parentKey: String) -> Int,
    itemsSize: (parentKey: String) -> Int,
    addFolder: AddFolder,
    clothesCategories: ImmutableList<ClothesCategory> = if (subMenu.type == SubMenuType.Closet) rememberClosetClothesCategory()
    else rememberWishClothesCategory(),
    state: DrawerItemState,
    folders: @Composable (parentKey: String, basePadding: Dp) -> Unit
) {
    DrawerRow(Modifier.padding(start = 8.dp), onClick = { onClick(subMenu.type) }) {
        DrawerDotIcon()
        DrawerText(
            text = subMenu.name, style = MaterialTheme.typography.subtitle1.copy(fontSize = 18.sp)
        )
        DrawerExpandIcon(expanded = { state.expanded }, toggleExpand = state::toggleExpand)
    }

    DrawerItemsWrapperWithExpandAnimation(expand = { state.expanded }, item = {
        Column(modifier = Modifier.background(Black18)) {
            clothesCategories.forEach { clothesCategory ->
                key(clothesCategory.type) {
                    val subState = rememberDrawerItemDropdownMenuState()

                    DrawerClothesSubMenu2(
                        state = subState,
                        clothesCategory = clothesCategory,
                        onClick = onClothesCategoryClick,
                        foldersSize = foldersSize,
                        folderNames = folderNames,
                        itemsSize = itemsSize,
                        addFolder = addFolder,
                        folders = folders
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

data class ClothesCategory(
    @StringRes val name: Int, val type: ClothesCategoryType
)

@Composable
fun rememberClosetClothesCategory() = remember {
    persistentListOf(
        ClothesCategory(R.string.tops_cap, ClothesCategoryType.Closet.Top),
        ClothesCategory(R.string.bottoms_cap, ClothesCategoryType.Closet.Bottom),
        ClothesCategory(R.string.outers_cap, ClothesCategoryType.Closet.Outer),
        ClothesCategory(R.string.etc_cap, ClothesCategoryType.Closet.Etc),
    )
}

@Composable
fun rememberWishClothesCategory() = remember {
    persistentListOf(
        ClothesCategory(R.string.tops_cap, ClothesCategoryType.Wish.Top),
        ClothesCategory(R.string.bottoms_cap, ClothesCategoryType.Wish.Bottom),
        ClothesCategory(R.string.outers_cap, ClothesCategoryType.Wish.Outer),
        ClothesCategory(R.string.etc_cap, ClothesCategoryType.Wish.Etc),
    )
}

