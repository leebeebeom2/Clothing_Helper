package com.leebeebeom.clothinghelperdomain.model

data class EssentialMenu(
    val id: Int,
    val name: Int,
    val drawable: Int,
)

data class MainCategory(
    val id: Int,
    val name: Int,
    val type: SubCategoryParent
)

object BaseMenuIds {
    const val MAIN_SCREEN = 0
    const val FAVORITE = 1
    const val SEE_ALL = 2
    const val TRASH = 3
}