package com.leebeebeom.clothinghelper.ui.main.root.model

import com.leebeebeom.clothinghelper.R
import kotlinx.collections.immutable.toImmutableList

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
