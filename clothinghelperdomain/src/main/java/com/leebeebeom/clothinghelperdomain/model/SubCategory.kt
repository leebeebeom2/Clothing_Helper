package com.leebeebeom.clothinghelperdomain.model

enum class SubCategoryParent {
    TOP, BOTTOM, OUTER, ETC
}

data class SubCategory(
    val parent: SubCategoryParent = SubCategoryParent.TOP,
    val id: Long = 0,
    val name: String = ""
)
