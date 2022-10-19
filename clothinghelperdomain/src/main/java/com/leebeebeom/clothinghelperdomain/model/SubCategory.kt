package com.leebeebeom.clothinghelperdomain.model

enum class SubCategoryParent {
    Top, Bottom, OUTER, ETC
}

data class SubCategory(
    val parent: SubCategoryParent = SubCategoryParent.Top,
    val id: Long = 0,
    val name: String = ""
)
