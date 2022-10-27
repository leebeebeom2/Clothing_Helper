package com.leebeebeom.clothinghelperdomain.model

enum class SubCategoryParent {
    TOP, BOTTOM, OUTER, ETC
}

object SubCategoryParentIndex {
    const val TOP = 0
    const val BOTTOM = 1
    const val OUTER = 2
    const val ETC = 3
}

data class SubCategory(
    val parent: SubCategoryParent = SubCategoryParent.TOP,
    val key: String = "",
    val name: String = ""
)
