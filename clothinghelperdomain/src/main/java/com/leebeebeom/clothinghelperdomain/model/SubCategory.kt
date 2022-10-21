package com.leebeebeom.clothinghelperdomain.model

enum class SubCategoryParent {
    Top, Bottom, OUTER, ETC
}

data class SubCategory(
    val parent: SubCategoryParent,
    val id: Long ,
    val name: String
)
