package com.leebeebeom.clothinghelperdomain.model

enum class EssentialMenus{
    MAIN_SCREEN, FAVORITE,SEE_ALL,TRASH
}

data class EssentialMenu(
    val name: Int,
    val drawable: Int,
    val type: EssentialMenus
)

data class MainCategory(val name: Int, val type: SubCategoryParent)