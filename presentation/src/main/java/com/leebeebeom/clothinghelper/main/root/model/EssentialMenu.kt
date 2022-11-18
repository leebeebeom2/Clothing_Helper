package com.leebeebeom.clothinghelper.main.root.model

import com.leebeebeom.clothinghelper.R
import kotlinx.collections.immutable.toImmutableList

data class EssentialMenu(
    val name: Int, val drawable: Int, val type: EssentialMenus
)

enum class EssentialMenus {
    MainScreen, Favorite, SeeAll, Trash
}

fun getEssentialMenus() =
    listOf(
        EssentialMenu(R.string.main_screen, R.drawable.ic_home, EssentialMenus.MainScreen),
        EssentialMenu(R.string.favorite, R.drawable.ic_star, EssentialMenus.Favorite),
        EssentialMenu(R.string.see_all, R.drawable.ic_list, EssentialMenus.SeeAll),
        EssentialMenu(R.string.trash, R.drawable.ic_delete, EssentialMenus.Trash)
    ).toImmutableList()
