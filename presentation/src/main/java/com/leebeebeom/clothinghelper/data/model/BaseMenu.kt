package com.leebeebeom.clothinghelper.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.leebeebeom.clothinghelper.R

data class EssentialMenu(
    val id: Int,
    @StringRes val name: Int,
    @DrawableRes val drawable: Int,
)

object BaseMenu {
    const val MAIN_SCREEN = 0
    const val FAVORITE = 1
    const val SEE_ALL = 2
    const val TRASH = 3
    const val TOP = 4
    const val BOTTOM = 5
    const val OUTER = 6
    const val ETC = 7

    val essentialMenus = listOf(
        EssentialMenu(MAIN_SCREEN, R.string.main_screen, R.drawable.ic_home),
        EssentialMenu(FAVORITE, R.string.favorite, R.drawable.ic_star),
        EssentialMenu(SEE_ALL, R.string.see_all, R.drawable.ic_list),
        EssentialMenu(TRASH, R.string.trash, R.drawable.ic_delete),
    )

    val mainCategories = listOf(
        EssentialMenu(TOP, R.string.top, R.drawable.ic_list),
        EssentialMenu(BOTTOM, R.string.bottom, R.drawable.ic_list),
        EssentialMenu(OUTER, R.string.outer, R.drawable.ic_list),
        EssentialMenu(ETC, R.string.etc, R.drawable.ic_list),
    )
}