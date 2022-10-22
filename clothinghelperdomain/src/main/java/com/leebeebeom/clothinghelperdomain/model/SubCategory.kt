package com.leebeebeom.clothinghelperdomain.model

enum class SubCategoryParent {
    TOP, BOTTOM, OUTER, ETC
}

data class SubCategory(
    val parentId: SubCategoryParent = SubCategoryParent.TOP,
    val id: Long = 0,
    val name: String = ""
)
