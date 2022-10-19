package com.leebeebeom.clothinghelperdomain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.R

data class EssentialMenu(
    val id: Int,
    @StringRes val name: Int,
    @DrawableRes val drawable: Int,
)

class MainCategory(
    val id: Int,
    @StringRes val name: Int,
    children: List<SubCategory> = emptyList()
) {
    var children by mutableStateOf(children)
    private set

    fun updateChildren(children: List<SubCategory>) {
        this.children = children
    }
}

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
}