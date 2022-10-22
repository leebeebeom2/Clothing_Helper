package com.leebeebeom.clothinghelperdomain.model

object SubCategoryParent {
    const val TOP = 4
    const val BOTTOM = 5
    const val OUTER = 6
    const val ETC = 7
}

data class SubCategory(
    val parentId: Int = SubCategoryParent.TOP,
    val id: Long = 0,
    val name: String = ""
)
