package com.leebeebeom.clothinghelper.data.model

enum class SubCategoryParent {
    Top, Bottom, OUTER, ETC
}

data class SubCategory(
    val parent: SubCategoryParent,
    val id: Long,
    val name: String
)
