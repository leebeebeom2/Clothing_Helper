package com.leebeebeom.clothinghelper.ui.drawer.component.submenu.closet

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemState
import com.leebeebeom.clothinghelper.ui.drawer.component.*
import com.leebeebeom.clothinghelper.ui.drawer.component.submenu.SubMenu
import com.leebeebeom.clothinghelper.ui.drawer.component.submenu.SubMenuType
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
    clothesCategories: ImmutableList<ClothesCategory> =
        if (subMenu.type == SubMenuType.Closet) rememberClosetClothesCategory()
        else rememberWishClothesCategory(),
    state: DrawerItemState,
    folders: @Composable (parentKey: String) -> Unit
) {
    DrawerRow(
        Modifier.padding(start = 8.dp),
        onClick = { onClick(subMenu.type) }) {
        DrawerDotIcon()
        DrawerText(
            text = subMenu.name,
            style = MaterialTheme.typography.subtitle1.copy(fontSize = 18.sp)
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

enum class ClothesCategoryType {
    ClosetTop, ClosetBottom, ClosetOuter, ClosetEtc, WishTop, WishBottom, WishOuter, WishEtc,
}

data class ClothesCategory(
    @StringRes val name: Int,
    val type: ClothesCategoryType
)

@Composable
fun rememberClosetClothesCategory() = remember {
    persistentListOf(
        ClothesCategory(R.string.tops_cap, ClothesCategoryType.ClosetTop),
        ClothesCategory(R.string.bottoms_cap, ClothesCategoryType.ClosetBottom),
        ClothesCategory(R.string.outers_cap, ClothesCategoryType.ClosetOuter),
        ClothesCategory(R.string.etc_cap, ClothesCategoryType.ClosetEtc),
    )
}

@Composable
fun rememberWishClothesCategory() = remember {
    persistentListOf(
        ClothesCategory(R.string.tops_cap, ClothesCategoryType.WishTop),
        ClothesCategory(R.string.bottoms_cap, ClothesCategoryType.WishBottom),
        ClothesCategory(R.string.outers_cap, ClothesCategoryType.WishOuter),
        ClothesCategory(R.string.etc_cap, ClothesCategoryType.WishEtc),
    )
}

